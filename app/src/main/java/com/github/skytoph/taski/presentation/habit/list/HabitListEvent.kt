package com.github.skytoph.taski.presentation.habit.list

import androidx.compose.runtime.MutableState
import com.github.skytoph.taski.presentation.habit.HabitUi

interface HabitListEvent {
    fun handle(state: MutableState<HabitListState>)

    class UpdateList(private val habits: List<HabitUi>) : HabitListEvent {

        override fun handle(state: MutableState<HabitListState>) {
            state.value = state.value.copy(habits = habits, isLoading = false)
        }
    }

    object Progress : HabitListEvent {
        override fun handle(state: MutableState<HabitListState>) {
            state.value = state.value.copy(isLoading = true)
        }
    }
}