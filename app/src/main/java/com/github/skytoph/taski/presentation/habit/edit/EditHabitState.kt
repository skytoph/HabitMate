package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.habit.icon.IconsColors

data class EditHabitState(
    val id: Long = HabitUi.ID_DEFAULT,
    val title: FieldState = FieldState(),
    val goal: GoalState = GoalState(),
    val icon: IconResource = IconResource.Default,
    val color: Color = IconsColors.Default,
    val isHistoryEditable: Boolean = false,
    val isLoading: Boolean = false,
    val isDialogShown: Boolean = false,
    val isValidated: Boolean = false
) {

    fun toHabitUi() = HabitUi(
        id = id,
        title = title.field,
        goal = goal.value,
        icon = icon,
        color = color,
    )
}