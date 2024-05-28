package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.core.state.StringResource
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
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

    class SelectFrequency(private val type: FrequencyState) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(frequency = type)
        }
    }

    abstract class UpdateFrequencyTimes(add: Int) : EditHabitEvent,
        UpdateFrequency.UpdateTimes(add) {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            update(state.value.frequency).let { state.value = state.value.copy(frequency = it) }
        }
    }

    object IncreaseFrequencyTimes : UpdateFrequencyTimes(1)

    object DecreaseFrequencyTimes : UpdateFrequencyTimes(-1)

    abstract class UpdateFrequencyType(add: Int) : EditHabitEvent, UpdateFrequency.UpdateType(add) {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            update(state.value.frequency).let { state.value = state.value.copy(frequency = it) }
        }
    }

    object IncreaseFrequencyType : UpdateFrequencyType(1)

    object DecreaseFrequencyType : UpdateFrequencyType(-1)
}

interface UpdateFrequency {
    fun update(frequency: FrequencyState): FrequencyState

    abstract class UpdateTimes(private val add: Int) : UpdateFrequency {
        override fun update(frequency: FrequencyState): FrequencyState {
            return if (frequency is FrequencyState.Custom) {
                val value = frequency.timesCount.value + add
                val times = frequency.frequencyType.times(value, frequency.typeCount.value)
                frequency.copy(timesCount = times)
            } else frequency
        }
    }

    abstract class UpdateType(private val add: Int) : UpdateFrequency {
        override fun update(frequency: FrequencyState): FrequencyState {
            return if (frequency is FrequencyState.Custom) {
                val value = frequency.typeCount.value + add
                val type = frequency.frequencyType.type(frequency.timesCount.value, value)
                val times = frequency.frequencyType.times(frequency.timesCount.value, value)
                frequency.copy(typeCount = type, timesCount = times)
            } else frequency
        }
    }
}