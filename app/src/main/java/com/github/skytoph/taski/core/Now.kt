package com.github.skytoph.taski.core

import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit

interface Now {
    fun dayOfWeek(): Int
    fun dayOfWeek(todayDayOfWeek: Int, daysAgo: Int): Int
    fun dayOfMonths(daysAgo: Int): Int
    fun milliseconds(): Long
    fun daysAgo(milliseconds: Long): Int
    fun daysAgoInMillis(days: Int): Long
    fun dayInMillis(): Long
    fun lastDayOfWeekDate(weeksAgo: Int = 0): Int
    fun lastDayOfWeekMillis(weeksAgo: Int = 0): Long
    fun monthMillis(monthsAgo: Int = 0): Long
    fun weeksInMonth(monthsAgo: Int = 0): Int
    fun daysInMonth(daysAgo: Int): Int

    class Base(private val timeZone: TimeZone = TimeZone.getTimeZone("UTC")) : Now {

        override fun dayOfWeek(): Int =
            calendar().run { (get(Calendar.DAY_OF_WEEK) - firstDayOfWeek + 7) % 7 }

        override fun dayOfWeek(todayDayOfWeek: Int, daysAgo: Int): Int =
            (6 + todayDayOfWeek - daysAgo % 7) % 7 + 1

        override fun milliseconds(): Long = System.currentTimeMillis()

        override fun daysAgoInMillis(days: Int): Long = startOfTheDay(days).timeInMillis

        override fun dayOfMonths(daysAgo: Int): Int =
            startOfTheDay(daysAgo).get(Calendar.DAY_OF_MONTH)

        override fun daysInMonth(daysAgo: Int): Int =
            startOfTheDay(daysAgo).getActualMaximum(Calendar.DAY_OF_MONTH)

        override fun daysAgo(milliseconds: Long): Int =
            TimeUnit.MILLISECONDS.toDays(dayInMillis() - milliseconds).toInt()

        override fun dayInMillis(): Long = startOfTheDay().timeInMillis

        override fun lastDayOfWeekDate(weeksAgo: Int): Int =
            endOfTheWeek(weeksAgo).get(Calendar.DAY_OF_MONTH)

        override fun lastDayOfWeekMillis(weeksAgo: Int): Long =
            endOfTheWeek(weeksAgo).timeInMillis

        override fun monthMillis(monthsAgo: Int): Long = month(monthsAgo).timeInMillis

        override fun weeksInMonth(monthsAgo: Int): Int =
            month(monthsAgo).getActualMaximum(Calendar.WEEK_OF_MONTH)

        private fun calendar(): Calendar = Calendar.getInstance(timeZone)

        private fun startOfTheDay(daysAgo: Int = 0): Calendar = calendar().also {
            it.set(Calendar.HOUR_OF_DAY, 0)
            it.set(Calendar.MINUTE, 0)
            it.set(Calendar.SECOND, 0)
            it.set(Calendar.MILLISECOND, 0)
            it.add(Calendar.DAY_OF_YEAR, -daysAgo)
        }

        private fun endOfTheWeek(weeksAgo: Int = 0): Calendar = startOfTheDay().also { calendar ->
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            calendar.add(Calendar.DATE, 6 - weeksAgo * 7)
        }

        private fun month(monthsAgo: Int): Calendar = calendar().also { calendar ->
            calendar.add(Calendar.MONTH, -monthsAgo)
        }
    }
}