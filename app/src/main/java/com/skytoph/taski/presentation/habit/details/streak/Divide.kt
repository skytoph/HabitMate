package com.skytoph.taski.presentation.habit.details.streak

import com.skytoph.taski.core.Now

interface Divide {
    fun divide(daysAgo: Int): Int

    class Week(private val now: Now, private val weeks: Int) : Divide, TransformWeekDay by TransformWeekDay.Base(now) {

        override fun divide(daysAgo: Int): Int =
            (daysAgo + transform(now.dayOfWeek(now.default, daysAgo)) - 1) / (7 * weeks)
    }

    class Month(private val now: Now, private val months: Int) : Divide {

        override fun divide(daysAgo: Int): Int = now.monthsAgo(daysAgo) / months
    }
}