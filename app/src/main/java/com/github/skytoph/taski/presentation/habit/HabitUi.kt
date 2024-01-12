package com.github.skytoph.taski.presentation.habit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class HabitUi(
    val title: String,
    val goal: Int,
    val icon: ImageVector,
    val color: Color,
    val history: List<Int> = emptyList(),
    val todayPositions: Int = MAX_DAYS - 1
) {

    fun isDoneToday(): Boolean = history.contains(todayPositions)

    companion object {
        const val MIN_GOAL: Int = 1
        const val MAX_GOAL: Int = 30
        const val MAX_DAYS: Int = 350
    }
}