package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now

interface CalculateInterval : StartAndEnd {

    class Week(
        private val now: Now,
        private val calculatePosition: StartAndEnd = StartAndEnd.Week(now),
        private val offset: Int = 0
    ) : CalculateInterval, Divide by Divide.Week(now, 1) {

        override fun start(daysAgo: Int): Int = calculatePosition.start(weeksAgo(daysAgo))
        override fun end(daysAgo: Int): Int = calculatePosition.end(weeksAgo(daysAgo) - offset)
        private fun weeksAgo(daysAgo: Int): Int = (daysAgo + now.dayOfWeek(now.default, daysAgo) - 1) / 7
    }

    class Month(
        private val now: Now,
        private val calculatePosition: StartAndEnd = StartAndEnd.Month(now),
        private val offset: Int = 0
    ) : CalculateInterval, Divide by Divide.Month(now, 1) {

        override fun start(daysAgo: Int): Int = calculatePosition.start(monthsAgo(daysAgo))
        override fun end(daysAgo: Int): Int = calculatePosition.end(monthsAgo(daysAgo) - offset)
        private fun monthsAgo(daysAgo: Int): Int = now.monthsAgo(daysAgo)
    }
}