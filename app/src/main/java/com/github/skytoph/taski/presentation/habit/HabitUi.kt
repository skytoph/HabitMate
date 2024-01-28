package com.github.skytoph.taski.presentation.habit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper

data class HabitUi<T : HabitHistoryUi>(
    val id: Long = ID_DEFAULT,
    val title: String,
    val goal: Int = 1,
    val icon: ImageVector,
    val color: Color,
    val history: T,
) {

    fun map(mapper: HabitDomainMapper) = mapper.map(id, title, goal, icon, color)

    companion object {
        const val MIN_GOAL: Int = 1
        const val MAX_GOAL: Int = 30
        const val ID_DEFAULT: Long = -1L
    }
}

interface HabitHistoryUi {
    object Empty : HabitHistoryUi
}

interface HabitEntryUi