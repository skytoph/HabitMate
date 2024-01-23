package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.presentation.habit.HabitScreens
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitToUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditHabitViewModel @Inject constructor(
    private val state: MutableState<EditHabitState> = mutableStateOf(EditHabitState()),
    private val interactor: EditHabitInteractor,
    private val mapper: HabitToUiMapper<EntryEditableUi>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        onEvent(EditHabitEvent.Progress(true))
        savedStateHandle.get<Long>(HabitScreens.EditHabit.habitIdArg)?.let { id ->
            if (id != HabitUi.ID_DEFAULT) {
                interactor.habit(id).map { habit -> habit.map(mapper) }
                    .flowOn(Dispatchers.IO)
                    .withIndex()
                    .onEach {
                        if (it.index == 0) onEvent(EditHabitEvent.Init(it.value))
                        else onEvent(EditHabitEvent.UpdateHistory(it.value.history))
                    }
                    .launchIn(viewModelScope)
            }
        }
    }

    fun saveHabit() = viewModelScope.launch(Dispatchers.IO) {
        val habit = state.value.toHabitUi()
        interactor.insert(habit)
    }

    fun deleteHabit() = viewModelScope.launch(Dispatchers.IO) {
        interactor.delete(state.value.id)
    }

    fun habitDone(daysAgo: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.habitDone(state.value.toHabitUi(), daysAgo)
        }
    }

    fun onEvent(event: EditHabitEvent) = event.handle(state)

    fun state(): State<EditHabitState> = state
}