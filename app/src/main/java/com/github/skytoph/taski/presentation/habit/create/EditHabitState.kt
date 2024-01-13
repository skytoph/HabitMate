package com.github.skytoph.taski.presentation.habit.create

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.habit.HabitUi

data class EditHabitState(
    val title: FieldState = FieldState(),
    val goal: GoalState = GoalState(),
    val icon: ImageVector = IconsGroup.allGroups.first().icons.first(),
    val color: Color = IconsColors.allColors.first()
) {

    fun toHabitUi() = HabitUi(title = title.field, goal = goal.value, icon = icon, color = color)
}