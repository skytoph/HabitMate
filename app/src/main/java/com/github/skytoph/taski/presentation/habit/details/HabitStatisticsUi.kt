package com.github.skytoph.taski.presentation.habit.details

data class HabitStatisticsUi(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val total: Int = 0,
    val streaks: List<StreakUi> = emptyList(),
) {
    private val positions: Set<Int>
        get() = mutableSetOf<Int>().apply { streaks.forEach { addAll((it.start..it.end).toList()) } }

    val streaksLength: List<Int>
        get() = streaks.map { it.length }

    fun isInRange(daysAgo: Int): Boolean = positions.contains(daysAgo)
}

data class StreakUi(val length: Int, val start: Int, val end: Int)