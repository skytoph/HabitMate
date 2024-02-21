package com.github.skytoph.taski.presentation.habit.edit

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
import com.github.skytoph.taski.presentation.habit.icon.IconState
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditHabitViewModel @Inject constructor(
    private val state: MutableState<EditHabitState> = mutableStateOf(EditHabitState()),
    private val iconState: MutableState<IconState>,
    private val interactor: EditHabitInteractor,
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
        onEvent(EditHabitEvent.Progress(true))
        viewModelScope.launch(Dispatchers.IO) {
            val habit = habitMapper.map(interactor.habit(savedStateHandle.id()))
            withContext(Dispatchers.Main) { onEvent(EditHabitEvent.Init(habit)) }
        }
    }

    private fun applyAction(pagingData: PagingData<EditableHistoryUi>, action: UpdateEntryAction) =
        pagingData.map { data ->
            val index = data.entries.indexOfFirst { it.daysAgo == action.entry.daysAgo }
            if (index == -1) data
            else data.copy(entries = data.entries.toMutableList().also { it[index] = action.entry })
        }


    fun saveHabit() = viewModelScope.launch(Dispatchers.IO) {
        interactor.insert(state.value.toHabitUi())
    }

    fun deleteHabit() = viewModelScope.launch(Dispatchers.IO) {
        interactor.delete(state.value.id)
    }

    fun habitDone(daysAgo: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.habitDone(state.value.toHabitUi(), daysAgo)
            val entry = interactor.entryEditable(state.value.id, daysAgo)
            withContext(Dispatchers.Main) { UpdateEntryAction(entry).handle(actions) }
        }
    }

    fun onEvent(event: EditHabitEvent) = event.handle(state, iconState)

    fun state(): State<EditHabitState> = state

    fun iconState(): State<IconState> = iconState

    fun SavedStateHandle.id(): Long = this[HabitScreens.EditHabit.habitIdArg]
        ?: throw IllegalStateException("Edit Habit screen must contain habit id")
}