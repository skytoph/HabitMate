package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now

interface StartAndEnd {
    fun end(index: Int): Int

    fun start(index: Int): Int

    class Week(private val now: Now) : StartAndEnd {

        override fun start(weeksAgo: Int): Int = now.firstDayOfWeekDaysAgo(now.default, weeksAgo)

        override fun end(weeksAgo: Int): Int = now.lastDayOfWeekDaysAgo(now.default, weeksAgo)
    }

    class Month(private val now: Now) : StartAndEnd {

        override fun start(monthsAgo: Int): Int = now.firstDayOfMonthDaysAgo(monthsAgo)

        override fun end(monthsAgo: Int): Int = now.lastDayOfMonthDaysAgo(monthsAgo)
    }
}