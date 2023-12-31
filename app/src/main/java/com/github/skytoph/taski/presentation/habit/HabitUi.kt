package com.github.skytoph.taski.presentation.habit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class HabitUi(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val history: List<Int>,
    val todayPositions: Int
) {

    companion object {
        const val MAX_DAYS: Int = 350
    }
}