package com.github.skytoph.taski.presentation.habit.details

data class HabitStatistics(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val total: Int = 0,
    val streaks: List<Streak> = emptyList(),
)