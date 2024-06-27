package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now

class CalculateDailyStreak(
    private val now: Now,
    days: Set<Int>,
) : CalculateStreak.Iterable(days), CalculateInterval by CalculateInterval.Week(now) {

    override val maxDays: Int = 7

    override fun dayNumber(daysAgo: Int): Int = now.dayOfWeek(daysAgo)
}