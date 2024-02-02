package com.github.skytoph.taski.presentation.habit.edit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.presentation.habit.icon.IconsGroup

data class EditHabitState(
    val id: Long = HabitUi.ID_DEFAULT,
    val title: FieldState = FieldState(),
    val goal: GoalState = GoalState(),
    val icon: ImageVector = IconsGroup.allGroups.first().icons.first(),
    val color: Color = IconsColors.allColors.first(),
    val history: HistoryState = HistoryState(),
    val isLoading: Boolean = false,
    val isDialogShown: Boolean = false,
) {

    fun toHabitUi() = HabitUi(
        id = id,
        title = title.field,
        goal = goal.value,
        icon = icon,
        color = color,
    )
}

data class HistoryState(
    val entries: List<EntryEditableUi> = emptyList(),
    val months: List<MonthUi> = emptyList(),
    val isEditable: Boolean = false,
)