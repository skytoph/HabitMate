package com.github.skytoph.taski.presentation.habit.create

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.skytoph.taski.ui.state.FieldState

data class EditHabitState(
    val title: FieldState = FieldState(),
    val goal: GoalState = GoalState(),
    val icon: ImageVector = IconsGroup.allGroups.first().icons.first(),
    val color: Color = IconsColors.allColors.first()
)