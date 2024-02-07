package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.skytoph.taski.presentation.habit.HabitScreens
import com.github.skytoph.taski.presentation.habit.icon.IconState
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    val entries: Flow<PagingData<EditableHistoryUi>> =
        interactor.entries(savedStateHandle.id()).cachedIn(viewModelScope)

    init {
        onEvent(EditHabitEvent.Progress(true))
        viewModelScope.launch(Dispatchers.IO) {
            val habit = habitMapper.map(interactor.habit(savedStateHandle.id()))
            withContext(Dispatchers.Main) { onEvent(EditHabitEvent.Init(habit)) }
        }
    }

    fun saveHabit() = viewModelScope.launch(Dispatchers.IO) {
        interactor.insert(state.value.toHabitUi())
    }

    fun deleteHabit() = viewModelScope.launch(Dispatchers.IO) {
        interactor.delete(state.value.id)
    }

    fun habitDone(daysAgo: Int) = viewModelScope.launch(Dispatchers.IO) {
        interactor.habitDone(state.value.toHabitUi(), daysAgo)
    }

    fun onEvent(event: EditHabitEvent) = event.handle(state, iconState)

    fun state(): State<EditHabitState> = state

    fun iconState(): State<IconState> = iconState

    fun SavedStateHandle.id(): Long =
        this[HabitScreens.EditHabit.habitIdArg]
            ?: throw IllegalStateException("Edit Habit screen must contain habit id")
}