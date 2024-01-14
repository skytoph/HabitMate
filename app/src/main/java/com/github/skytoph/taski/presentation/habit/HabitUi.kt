package com.github.skytoph.taski.presentation.habit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper

data class HabitUi(
    val id: Long = ID_DEFAULT,
    val title: String,
    val goal: Int = 1,
    val icon: ImageVector,
    val color: Color,
    val history: Map<Int, Int> = emptyMap(),
    val todayPosition: Int = MAX_DAYS - 1,
    val todayDonePercent: Float = 0F,
) {

    fun map(mapper: HabitDomainMapper) = mapper.map(id, title, goal, icon, color)

    companion object {
        const val MIN_GOAL: Int = 1
        const val MAX_GOAL: Int = 30
        const val MAX_DAYS: Int = 350
        const val ID_DEFAULT: Long = -1L
    }
}