package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.core.state.StringResource
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyCustomType
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
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
                frequencyState = FrequencyState(selectedName = habit.frequency.name)
                    .updateSelected(habit.frequency)
            )
            icon.value = IconState(habit.icon, habit.color)
        }
    }

    class EditTitle(private val title: String) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value =
                state.value.copy(title = state.value.title.copy(field = title, error = null))
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

    class UpdateIcon(private val icon: IconResource? = null, private val color: Color? = null) :
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

    class TitleError(private val error: StringResource?) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(title = state.value.title.copy(error = error))
        }
    }

    object Validate : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(isValidated = true)
        }
    }

    object ExpandFrequency : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(isFrequencyExpanded = !state.value.isFrequencyExpanded)
        }
    }

    object ExpandCustomType : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(isCustomTypeExpanded = !state.value.isCustomTypeExpanded)
        }
    }

    class SelectFrequency(private val type: FrequencyUi) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value =
                state.value.copy(frequencyState = state.value.frequencyState.copy(selectedName = type.name))
        }
    }

    abstract class UpdateFrequencyTimes(private val add: Int) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            val updated = state.value.frequencyState.custom.update(add)
            state.value =
                state.value.copy(frequencyState = state.value.frequencyState.copy(custom = updated))
        }
    }

    object IncreaseFrequencyTimes : UpdateFrequencyTimes(1)

    object DecreaseFrequencyTimes : UpdateFrequencyTimes(-1)

    abstract class UpdateFrequencyType(private val add: Int) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            val updated = state.value.frequencyState.custom.updateType(add)
            state.value =
                state.value.copy(frequencyState = state.value.frequencyState.copy(custom = updated))
        }
    }

    object IncreaseFrequencyType : UpdateFrequencyType(1)

    object DecreaseFrequencyType : UpdateFrequencyType(-1)

    class SelectDay(private val day: Int) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            val updated = state.value.frequencyState.selected.update(day)
            state.value =
                state.value.copy(frequencyState = state.value.frequencyState.updateSelected(updated))
        }
    }

    class SelectCustomType(private val type: FrequencyCustomType) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(
                frequencyState = state.value.frequencyState.updateCustom(type),
                isCustomTypeExpanded = false
            )
        }
    }
}