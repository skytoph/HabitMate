package com.github.skytoph.taski.presentation.habit.create

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
import com.github.skytoph.taski.presentation.habit.icon.IconsColors

data class CreateHabitState(
    val title: FieldState = FieldState(),
    val goal: GoalState = GoalState(),
    val icon: IconResource = IconResource.Default,
    val color: Color = IconsColors.Default,
    val isValidated: Boolean = false,
    val frequencyState: FrequencyState = FrequencyState(),
    val isFrequencyExpanded: Boolean = true,
) {

    fun toHabitUi() = HabitUi(
        title = title.field,
        goal = goal.value,
        icon = icon,
        color = color,
        frequency = frequencyState.selected
    )
}