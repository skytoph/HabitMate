package com.skytoph.taski.presentation.habit.details

import com.skytoph.taski.presentation.habit.details.mapper.mapToUi

data class HabitStatistics(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val total: Int = 0,
    val streaks: List<Streak> = emptyList(),
) : HabitStatisticsResult {
    override fun map(): HabitStatisticsUi = mapToUi()
}

data class HabitState(
    val isStreakCurrently: Boolean = false,
    val isScheduledForToday: Boolean = true,
) : HabitStatisticsResult

interface HabitStatisticsResult {
    fun map(): HabitStatisticsUi = HabitStatisticsUi()
}