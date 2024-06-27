package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.presentation.habit.details.HabitStatistics
import com.github.skytoph.taski.presentation.habit.details.HabitStatisticsUi

fun HabitStatistics.map(): HabitStatisticsUi = HabitStatisticsUi(
    currentStreak = currentStreak,
    bestStreak = bestStreak,
    total = total,
    streaks = streaks.map { it.map() }
)