package com.skytoph.taski.presentation.habit.create

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource
import com.skytoph.taski.presentation.habit.edit.frequency.FrequencyCustomType
import com.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import com.skytoph.taski.presentation.habit.list.component.DialogItem

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

    class EditDescription(private val value: String) : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            state.value =
                state.value.copy(description = state.value.description.copy(field = value))
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
            state.value =
                state.value.copy(isFrequencyExpanded = !state.value.isFrequencyExpanded)
        }
    }

    object ExpandCustomType : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            state.value =
                state.value.copy(isCustomTypeExpanded = !state.value.isCustomTypeExpanded)
        }
    }

    class SelectFrequency(private val type: FrequencyUi) : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            state.value =
                state.value.copy(frequencyState = state.value.frequencyState.copy(selectedName = type.name))
        }
    }

    abstract class UpdateFrequencyTimes(private val add: Int) : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            val updated = state.value.frequencyState.custom.update(add)
            state.value =
                state.value.copy(frequencyState = state.value.frequencyState.copy(custom = updated))
        }
    }

    object IncreaseFrequencyTimes : UpdateFrequencyTimes(1)

    object DecreaseFrequencyTimes : UpdateFrequencyTimes(-1)

    abstract class UpdateFrequencyType(private val add: Int) : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            val updated = state.value.frequencyState.custom.updateType(add)
            state.value =
                state.value.copy(frequencyState = state.value.frequencyState.copy(custom = updated))
        }
    }

    object IncreaseFrequencyType : UpdateFrequencyType(1)

    object DecreaseFrequencyType : UpdateFrequencyType(-1)

    class SelectDay(private val day: Int) : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            val updated = state.value.frequencyState.selected.update(day)
            state.value =
                state.value.copy(frequencyState = state.value.frequencyState.updateSelected(updated))
        }
    }

    class SelectCustomType(private val type: FrequencyCustomType) : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            state.value = state.value.copy(
                frequencyState = state.value.frequencyState.updateCustom(type),
                isCustomTypeExpanded = false
            )
        }
    }

    class UpdateReminder(
        private val switchOn: Boolean? = null,
        private val showDialog: Boolean? = null,
        private val hour: Int? = null,
        private val minute: Int? = null
    ) : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            switchOn?.let {
                state.value =
                    state.value.copy(reminder = state.value.reminder.copy(switchedOn = it))
            }
            showDialog?.let {
                state.value =
                    state.value.copy(reminder = state.value.reminder.copy(isDialogShown = it))
            }
            hour?.let {
                state.value =
                    state.value.copy(reminder = state.value.reminder.copy(hour = it))
            }
            minute?.let {
                state.value =
                    state.value.copy(reminder = state.value.reminder.copy(minute = it))
            }
        }
    }

    class ShowPermissionDialog(private val dialog: DialogItem?) : CreateHabitEvent {
        override fun handle(state: MutableState<CreateHabitState>) {
            state.value = state.value.copy(dialog = dialog)
        }
    }
}