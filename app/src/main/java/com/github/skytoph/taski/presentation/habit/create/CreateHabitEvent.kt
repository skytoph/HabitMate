package com.github.skytoph.taski.presentation.habit.create

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.core.state.StringResource
import com.github.skytoph.taski.presentation.habit.edit.UpdateFrequency
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyState

interface CreateHabitEvent {
    fun handle(state: MutableState<CreateHabitState>)

    class EditTitle(private val title: String) : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            state.value =
                state.value.copy(title = state.value.title.copy(field = title, error = null))
        }
    }

    class TitleError(private val error: StringResource?) : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            state.value = state.value.copy(title = state.value.title.copy(error = error))
        }
    }

    object Validate : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            state.value = state.value.copy(isValidated = true)
        }
    }

    object IncreaseGoal : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            if (state.value.goal.canBeIncreased) {
                val newGoal = state.value.goal.value + 1
                state.value = state.value.copy(goal = GoalState(value = newGoal))
            }
        }
    }

    object DecreaseGoal : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            if (state.value.goal.canBeDecreased) {
                val newGoal = state.value.goal.value - 1
                state.value = state.value.copy(goal = GoalState(value = newGoal))
            }
        }
    }

    class UpdateIcon(
        private val icon: IconResource? = null,
        private val color: Color? = null
    ) : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            icon?.let { state.value = state.value.copy(icon = icon) }
            color?.let { state.value = state.value.copy(color = color) }
        }
    }

    object ExpandFrequency : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            state.value = state.value.copy(isFrequencyExpanded = !state.value.isFrequencyExpanded)
        }
    }

    class SelectFrequency(private val type: FrequencyState) : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            state.value = state.value.copy(frequency = type)
        }
    }

    abstract class UpdateFrequencyTimes(add: Int) : CreateHabitEvent,
        UpdateFrequency.UpdateTimes(add) {
        override fun handle(state: MutableState<CreateHabitState>) {
            update(state.value.frequency).let { state.value = state.value.copy(frequency = it) }
        }
    }

    object IncreaseFrequencyTimes : UpdateFrequencyTimes(1)

    object DecreaseFrequencyTimes : UpdateFrequencyTimes(-1)

    abstract class UpdateFrequencyType(add: Int) : CreateHabitEvent,
        UpdateFrequency.UpdateType(add) {
        override fun handle(state: MutableState<CreateHabitState>) {
            update(state.value.frequency).let { state.value = state.value.copy(frequency = it) }
        }
    }

    object IncreaseFrequencyType : UpdateFrequencyType(1)

    object DecreaseFrequencyType : UpdateFrequencyType(-1)
}