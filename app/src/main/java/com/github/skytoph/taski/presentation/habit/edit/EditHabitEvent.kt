package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.icon.IconState

interface EditHabitEvent {
    fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>)

    class Init(private val habit: HabitUi) : EditHabitEvent {

        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = EditHabitState(
                id = habit.id,
                title = FieldState(field = habit.title),
                goal = GoalState(habit.goal),
                icon = habit.icon,
                color = habit.color,
                isLoading = false,
            )
            icon.value = IconState(habit.icon, habit.color)
        }
    }

    object EditHistory : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(isHistoryEditable = !state.value.isHistoryEditable)
        }
    }

    class EditTitle(private val title: String) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(title = state.value.title.copy(field = title))
        }
    }

    object IncreaseGoal : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            if (state.value.goal.canBeIncreased) {
                val newGoal = state.value.goal.value + 1
                state.value = state.value.copy(goal = GoalState(value = newGoal))
            }
        }
    }

    object DecreaseGoal : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            if (state.value.goal.canBeDecreased) {
                val newGoal = state.value.goal.value - 1
                state.value = state.value.copy(goal = GoalState(value = newGoal))
            }
        }
    }

    class UpdateIcon(private val icon: ImageVector? = null, private val color: Color? = null) :
        EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            this.icon?.let { state.value = state.value.copy(icon = this.icon) }
            this.color?.let { state.value = state.value.copy(color = this.color) }
        }
    }

    class Progress(private val isLoading: Boolean) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(isLoading = isLoading)
        }
    }

    class ShowDialog(private val isDialogShown: Boolean) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(isDialogShown = isDialogShown)
        }
    }
}