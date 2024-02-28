package com.github.skytoph.taski.presentation.habit.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
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
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val actions = MutableStateFlow<List<UpdateEntryAction>>(emptyList())

    val entries: Flow<PagingData<EditableHistoryUi>> =
        interactor.entries(savedStateHandle.id()).cachedIn(viewModelScope)
            .combine(actions) { pagingData, actions ->
                actions.fold(pagingData) { paging, event -> applyAction(paging, event) }
            }

    init {
        interactor.habit(savedStateHandle.id())
            .map { habit -> habitMapper.map(habit) }
            .flowOn(Dispatchers.IO)
            .onEach { habit -> onEvent(HabitDetailsEvent.Init(habit)) }
            .launchIn(viewModelScope)
    }

    private fun applyAction(pagingData: PagingData<EditableHistoryUi>, action: UpdateEntryAction) =
        pagingData.map { data ->
            val index = data.entries.indexOfFirst { it.daysAgo == action.entry.daysAgo }
            if (index == -1) data
            else data.copy(entries = data.entries.toMutableList().also { it[index] = action.entry })
        }

    fun deleteHabit() = viewModelScope.launch(Dispatchers.IO) {
        state.value.habit?.id?.let { id -> interactor.delete(id) }
    }

    fun habitDone(daysAgo: Int) = state.value.habit?.let { habit ->
        viewModelScope.launch(Dispatchers.IO) {
            interactor.habitDone(habit, daysAgo)
            val entry = interactor.entryEditable(habit.id, daysAgo)
            withContext(Dispatchers.Main) { UpdateEntryAction(entry).handle(actions) }
        }
    }

    fun onEvent(event: HabitDetailsEvent) = event.handle(state)

    fun state(): State<HabitDetailsState> = state

    fun SavedStateHandle.id(): Long = this[HabitScreens.EditHabit.habitIdArg]
        ?: throw IllegalStateException("Edit Habit screen must contain habit id")
}