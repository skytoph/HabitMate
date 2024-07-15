package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now

interface StartAndEnd {
    fun end(index: Int): Int

    fun start(index: Int): Int

    class Week(private val now: Now) : StartAndEnd {

        override fun start(index: Int): Int = now.firstDayOfWeekDaysAgo(now.default, index)

        override fun end(index: Int): Int = now.lastDayOfWeekDaysAgo(now.default, index)
    }

    class Month(private val now: Now) : StartAndEnd {

        override fun start(index: Int): Int = now.firstDayOfMonthDaysAgo(index)

        override fun end(index: Int): Int = now.lastDayOfMonthDaysAgo(index)
    }
}