package com.skytoph.taski.presentation.habit.create

import androidx.compose.ui.graphics.Color
import com.skytoph.taski.presentation.core.state.FieldState
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.ReminderUi
import com.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
import com.skytoph.taski.presentation.habit.icon.IconsColors
import com.skytoph.taski.presentation.habit.list.component.DialogItem

data class CreateHabitState(
    val title: FieldState = FieldState(),
    val description: FieldState = FieldState(),
    val goal: GoalState = GoalState(),
    val icon: IconResource = IconResource.Default,
    val color: Color = IconsColors.Default,
    val isValidated: Boolean = false,
    val frequencyState: FrequencyState = FrequencyState(),
    val reminder: ReminderUi = ReminderUi(),
    val dialog: DialogItem? = null,
    val isFrequencyExpanded: Boolean = false,
    val isCustomTypeExpanded: Boolean = false,
) {

    fun toHabitUi() = HabitUi(
        title = title.field,
        description = description.field,
        goal = goal.value,
        icon = icon,
        color = color,
        frequency = frequencyState.selected,
        reminder = reminder
    )
}