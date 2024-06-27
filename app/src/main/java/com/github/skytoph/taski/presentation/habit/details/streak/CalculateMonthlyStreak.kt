package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now

class CalculateMonthlyStreak(private val now: Now, days: Set<Int>) :
    CalculateStreak.Iterable(days) {

    override val maxDays: Int = now.daysInMonth(dayNumber(0))

    override fun dayNumber(daysAgo: Int): Int = now.dayOfMonths(daysAgo)

    override fun findPosition(currentPosition: Int, nextValue: Int): Int {
        val currentValue = dayNumber(currentPosition)
        val daysInMonth = now.daysInMonth(currentPosition + currentValue)
        val result = currentPosition + currentValue - when {
            nextValue > 28 && currentValue < nextValue && daysInMonth < nextValue ->
                nextValue - daysInMonth

            currentValue < nextValue ->
                nextValue - dayNumber(currentPosition + currentValue)

            else -> nextValue
        }
        return result
    }
}