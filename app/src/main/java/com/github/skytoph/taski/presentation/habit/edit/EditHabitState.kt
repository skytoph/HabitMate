package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.ReminderUi
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.presentation.habit.list.component.DialogItem

data class EditHabitState(
    val id: Long = HabitUi.ID_DEFAULT,
    val title: FieldState = FieldState(),
    val description: FieldState = FieldState(),
    val goal: GoalState = GoalState(value = goalIsNotInitialized),
    val priority: Int = Int.MAX_VALUE,
    val frequencyState: FrequencyState = FrequencyState(),
    val reminder: ReminderUi = ReminderUi(),
    val isFrequencyExpanded: Boolean = false,
    val isCustomTypeExpanded: Boolean = false,
    val icon: IconResource = IconResource.Default,
    val color: Color = IconsColors.Default,
    val isHistoryEditable: Boolean = false,
    val isLoading: Boolean = false,
    val isValidated: Boolean = false,
    val dialog: DialogItem? = null
) {

    fun toHabitUi() = HabitUi(
        id = id,
        title = title.field,
        description = description.field,
        goal = goal.value,
        icon = icon,
        color = color,
        priority = priority,
        frequency = frequencyState.selected,
        reminder = reminder
    )

    companion object {
        const val goalIsNotInitialized: Int = 0
    }
}