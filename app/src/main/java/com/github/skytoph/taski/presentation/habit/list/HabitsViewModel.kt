package com.github.skytoph.taski.presentation.habit.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.domain.habit.HabitToUiMapper
import com.github.skytoph.taski.presentation.habit.HabitUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val state: MutableState<HabitListState>,
    private val repository: HabitRepository,
    private val mapper: HabitToUiMapper,
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.habits()
                .onEach { habits -> onEvent(HabitListEvent.UpdateList(habits.map { it.map(mapper) })) }
                .launchIn(viewModelScope)
        }
    }

    fun habitDone(habit: HabitUi) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun onEvent(event: HabitListEvent) = event.handle(state)

    fun state(): State<HabitListState> = state
}