package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now

class CalculateDailyStreak(private val now: Now, days: Set<Int>) : CalculateStreak.Abstract(days) {
    override fun dayNumber(daysAgo: Int): Int = now.dayOfWeek(daysAgo)
}