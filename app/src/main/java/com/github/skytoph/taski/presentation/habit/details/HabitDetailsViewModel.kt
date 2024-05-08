package com.github.skytoph.taski.presentation.habit.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.core.component.AppBarState
import com.github.skytoph.taski.presentation.habit.HabitScreens
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HabitDetailsViewModel @Inject constructor(
    private val state: MutableState<HabitDetailsState> = mutableStateOf(HabitDetailsState()),
    private val interactor: HabitDetailsInteractor,
    private val habitMapper: HabitUiMapper,
    private val savedStateHandle: SavedStateHandle,
    appBarState: MutableState<AppBarState>
) : ViewModel(), InitAppBar by InitAppBar.Base(appBarState) {

    private val actions = MutableStateFlow<List<UpdateEntryAction>>(emptyList())

    private var entries: Flow<PagingData<EditableHistoryUi>>? = null

    init {
        interactor.habit(savedStateHandle.id())
            .map { habit -> habit?.let { habitMapper.map(it) } }
            .flowOn(Dispatchers.IO)
            .onEach { it?.let { habit -> onEvent(HabitDetailsEvent.Init(habit)) } }
            .launchIn(viewModelScope)
    }

    private fun applyAction(pagingData: PagingData<EditableHistoryUi>, action: UpdateEntryAction) =
        pagingData.map { data -> interactor.mapData(data, action.entry) }

    fun deleteHabit(navigateUp: () -> Unit) {
        val id = state.value.habit?.id
        state.clear()
        viewModelScope.launch(Dispatchers.IO) {
            id?.let { id -> interactor.delete(id) }
            withContext(Dispatchers.Main) { navigateUp() }
        }
    }

    fun habitDone(daysAgo: Int, defaultColor: Color) = state.value.habit?.let { habit ->
        viewModelScope.launch(Dispatchers.IO) {
            interactor.habitDone(habit, daysAgo)
            val entry =
                interactor.entryEditable(habit.id, daysAgo, habit.goal, habit.color, defaultColor)
            withContext(Dispatchers.Main) { UpdateEntryAction(entry).handle(actions) }
        }
    }

    fun onEvent(event: HabitDetailsEvent) = event.handle(state)

    fun state(): State<HabitDetailsState> = state

    fun SavedStateHandle.id(): Long = this[HabitScreens.EditHabit.habitIdArg]
        ?: throw IllegalStateException("Edit Habit screen must contain habit id")

    fun entries(defaultColor: Color): Flow<PagingData<EditableHistoryUi>> = entries ?: run {
        entries = interactor.entries(savedStateHandle.id(), defaultColor).cachedIn(viewModelScope)
            .combine(actions) { pagingData, actions ->
                actions.fold(pagingData) { paging, event -> applyAction(paging, event) }
            }
        entries!!
    }
}