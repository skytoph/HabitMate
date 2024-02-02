package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.presentation.habit.HabitScreens
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.icon.IconState
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditHabitViewModel @Inject constructor(
    private val state: MutableState<EditHabitState> = mutableStateOf(EditHabitState()),
    private val iconState: MutableState<IconState>,
    private val interactor: EditHabitInteractor,
    private val habitMapper: HabitUiMapper,
    private val historyMapper: HabitHistoryUiMapper<EditableHistoryUi>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        onEvent(EditHabitEvent.Progress(true))
        savedStateHandle.get<Long>(HabitScreens.EditHabit.habitIdArg)?.let { id ->
            if (id != HabitUi.ID_DEFAULT) {
                viewModelScope.launch(Dispatchers.IO) {
                    val habit = habitMapper.map(interactor.habit(id))
                    withContext(Dispatchers.Main) { onEvent(EditHabitEvent.Init(habit)) }
                }
                interactor.history(id)
                    .map { history -> historyMapper.map(history = history) }
                    .flowOn(Dispatchers.IO)
                    .onEach { onEvent(EditHabitEvent.UpdateHistory(it)) }
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

    fun onEvent(event: EditHabitEvent) = event.handle(state, iconState)

    fun state(): State<EditHabitState> = state

    fun iconState(): State<IconState> = iconState
}