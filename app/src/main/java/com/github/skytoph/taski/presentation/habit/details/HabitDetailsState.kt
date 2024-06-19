package com.github.skytoph.taski.presentation.habit.details

import androidx.compose.runtime.MutableState
import com.github.skytoph.taski.presentation.habit.HabitUi

data class HabitDetailsState(
    val habit: HabitUi? = null,
    val isHistoryEditable: Boolean = false,
    val isDeleteDialogShown: Boolean = false,
    val statistics: HabitStatisticsUi = HabitStatisticsUi()
)

fun MutableState<HabitDetailsState>.clear() {
    this.value = HabitDetailsState()
}