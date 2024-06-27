package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now

interface Divide {
    fun divide(daysAgo: Int): Int

    class Week(private val now: Now, private val weeks: Int) : Divide {

        override fun divide(daysAgo: Int): Int =
            (daysAgo + now.dayOfWeek(daysAgo) - 1) / (7 * weeks)

    }

    class Month(private val now: Now, private val months: Int) : Divide {

        override fun divide(daysAgo: Int): Int = now.monthsAgo(daysAgo) / months
    }
}