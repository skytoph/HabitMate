package com.github.skytoph.taski.presentation.habit.create

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.presentation.habit.icon.IconsGroup

data class EditHabitState(
    val title: FieldState = FieldState(),
    val goal: GoalState = GoalState(),
    val icon: ImageVector = IconsGroup.allGroups.first().icons.first(),
    val color: Color = IconsColors.allColors.first(),
    val isLoading: Boolean = false,
    val isNewHabit: Boolean = true,
    val isDialogShown: Boolean = false,
) {

    fun toHabitUi(id: Long) =
        HabitUi(id = id, title = title.field, goal = goal.value, icon = icon, color = color)
}