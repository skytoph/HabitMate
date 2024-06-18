package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now

class CalculateDailyStreak(private val now: Now) : CalculateStreak.Abstract() {
    private val today = now.dayOfWeek() + 1
    override fun dayNumber(daysAgo: Int): Int = now.dayOfWeek(today, daysAgo)
}