package com.github.skytoph.taski.presentation.habit.create

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.habit.HabitUi

interface EditHabitEvent {
    fun handle(state: MutableState<EditHabitState>)

    class Init(private val habit: HabitUi) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>) {
            state.value = EditHabitState(
                title = FieldState(field = habit.title),
                goal = GoalState(habit.goal),
                icon = habit.icon,
                color = habit.color,
                isLoading = false
            )
        }
    }

    class EditTitle(private val title: String) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>) {
            state.value = state.value.copy(title = state.value.title.copy(field = title))
        }
    }

    object IncreaseGoal : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>) {
            if (state.value.goal.canBeIncreased) {
                val newGoal = state.value.goal.value + 1
                state.value = state.value.copy(goal = GoalState(value = newGoal))
            }
        }
    }

    object DecreaseGoal : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>) {
            if (state.value.goal.canBeDecreased) {
                val newGoal = state.value.goal.value - 1
                state.value = state.value.copy(goal = state.value.goal.copy(value = newGoal))
            }
        }
    }

    class UpdateIcon(
        private val icon: ImageVector? = null,
        private val color: Color? = null
    ) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>) {
            icon?.let { state.value = state.value.copy(icon = icon) }
            color?.let { state.value = state.value.copy(color = color) }
        }
    }

    object Clear : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>) {
            state.value = EditHabitState()
        }
    }

    class Progress(private val isLoading: Boolean) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>) {
            state.value = state.value.copy(isLoading = isLoading)
        }
    }
}