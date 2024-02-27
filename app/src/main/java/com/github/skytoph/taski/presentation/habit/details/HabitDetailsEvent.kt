package com.github.skytoph.taski.presentation.habit.details

import androidx.compose.runtime.MutableState
import com.github.skytoph.taski.presentation.habit.HabitUi

interface HabitDetailsEvent {
    fun handle(state: MutableState<HabitDetailsState>)

    class Init(private val habit: HabitUi) : HabitDetailsEvent {
        override fun handle(state: MutableState<HabitDetailsState>) {
            state.value = HabitDetailsState(habit = habit)
        }
    }

    object EditHistory : HabitDetailsEvent {
        override fun handle(state: MutableState<HabitDetailsState>) {
            state.value = state.value.copy(isHistoryEditable = !state.value.isHistoryEditable)
        }
    }

    class ShowDialog(private val isDialogShown: Boolean) : HabitDetailsEvent {
        override fun handle(state: MutableState<HabitDetailsState>) {
            state.value = state.value.copy(isDeleteDialogShown = isDialogShown)
        }
    }
}
