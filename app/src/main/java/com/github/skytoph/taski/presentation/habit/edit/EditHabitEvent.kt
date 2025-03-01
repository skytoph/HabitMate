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
import com.github.skytoph.taski.presentation.habit.list.component.DialogItem

interface EditHabitEvent {
    fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>)

    class Init(private val habit: HabitUi) : EditHabitEvent {

        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(
                id = habit.id,
                title = FieldState(field = habit.title),
                description = FieldState(field = habit.description),
                goal = GoalState(habit.goal),
                icon = habit.icon,
                color = habit.color,
                priority = habit.priority,
                isLoading = false,
                frequencyState = FrequencyState(selectedName = habit.frequency.name).updateSelected(habit.frequency),
                reminder = habit.reminder
            )
            icon.value = IconState(habit.icon, habit.color)
        }
    }

    class AllowReminder(private val allowed: Boolean) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(reminderAllowed = allowed)
        }
    }

    class EditTitle(private val title: String) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value =
                state.value.copy(title = state.value.title.copy(field = title, error = null))
        }
    }

    class EditDescription(private val value: String) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value =
                state.value.copy(description = state.value.description.copy(field = value))
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

    class UpdateReminder(
        private val switchOn: Boolean? = null,
        private val showDialog: Boolean? = null,
        private val hour: Int? = null,
        private val minute: Int? = null
    ) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
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

    class ShowPermissionDialog(private val dialog: DialogItem? = null) : EditHabitEvent {
        override fun handle(state: MutableState<EditHabitState>, icon: MutableState<IconState>) {
            state.value = state.value.copy(dialog = dialog)
        }
    }
}