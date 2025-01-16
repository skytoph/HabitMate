package com.github.skytoph.taski.presentation.habit.details

import com.github.skytoph.taski.presentation.habit.edit.StreakType

data class HabitStatisticsUi(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val total: Int = 0,
    val streaks: List<StreakUi> = emptyList(),
) {

    private val streakTypes: Map<Int, StreakType> =
        mutableMapOf<Int, StreakType>().apply {
            streaks.forEach { streak ->
                if (streak.start == streak.end)
                    put(streak.start, StreakType.Dot)
                else {
                    val types: Map<Int, StreakType> = mapOf(streak.start to StreakType.Start) +
                            (streak.start + 1 until streak.end).associateWith { StreakType.Middle } +
                            (streak.end to StreakType.End)
                    putAll(types)
                }
            }
        }

    fun type(daysAgo: Int): StreakType? = streakTypes[daysAgo]
}

data class StreakUi(val length: Int, val start: Int, val end: Int)