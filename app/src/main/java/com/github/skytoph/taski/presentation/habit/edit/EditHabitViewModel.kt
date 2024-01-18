package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.domain.habit.HabitToUiMapper
import com.github.skytoph.taski.presentation.habit.HabitScreens
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditHabitViewModel @Inject constructor(
    private val state: MutableState<EditHabitState> = mutableStateOf(EditHabitState()),
    private val interactor: EditHabitInteractor,
    private val mapper: HabitDomainMapper,
    private val mapperUi: HabitToUiMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        onEvent(EditHabitEvent.Progress(true))
        savedStateHandle.get<Long>(HabitScreens.EditHabit.habitIdArg)?.let { id ->
            if (id != HabitUi.ID_DEFAULT) {
                interactor.habit(id).map { it.map(mapperUi) }
                    .flowOn(Dispatchers.IO)
                    .onEach { onEvent(EditHabitEvent.Init(it)) }
                    .launchIn(viewModelScope)
            }
        }
    }

    fun saveHabit() = viewModelScope.launch(Dispatchers.IO) {
        val habit = state.value.toHabitUi().map(mapper)
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