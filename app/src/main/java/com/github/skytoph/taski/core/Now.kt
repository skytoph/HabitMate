package com.github.skytoph.taski.core

import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit

interface Now {
    fun dayOfWeek(): Int
    fun dayOfWeek(daysAgo: Int): Int
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
    fun monthsAgo(daysAgo: Int): Int
    fun lastDayOfWeekDaysAgo(weeksAgo: Int): Int
    fun lastDayOfMonthDaysAgo(monthsAgo: Int): Int
    fun firstDayOfMonthDaysAgo(monthsAgo: Int): Int
    fun firstDayOfWeekDaysAgo(weeksAgo: Int): Int

    class Base(private val timeZone: TimeZone = TimeZone.getTimeZone("UTC")) : Now {

        override fun monthsAgo(daysAgo: Int): Int {
            val compareWith = calendar().apply { add(Calendar.DAY_OF_YEAR, -daysAgo) }
            val today = calendar()
            return 12 * (today.get(Calendar.YEAR) - compareWith.get(Calendar.YEAR)) +
                    today.get(Calendar.MONTH) - compareWith.get(Calendar.MONTH)
        }

        override fun dayOfWeek(): Int =
            calendar().run { (get(Calendar.DAY_OF_WEEK) - firstDayOfWeek + 7) % 7 } + 1

        override fun dayOfWeek(daysAgo: Int): Int =
            (6 + dayOfWeek() - daysAgo % 7) % 7 + 1

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

        override fun lastDayOfMonthDaysAgo(monthsAgo: Int): Int =
            TimeUnit.MILLISECONDS.toDays(dayInMillis() - endOfTheMonth(monthsAgo).timeInMillis)
                .toInt()

        override fun lastDayOfWeekDaysAgo(weeksAgo: Int): Int =
            TimeUnit.MILLISECONDS.toDays(dayInMillis() - endOfTheWeek(weeksAgo).timeInMillis)
                .toInt()

        override fun firstDayOfMonthDaysAgo(monthsAgo: Int): Int =
            TimeUnit.MILLISECONDS.toDays(dayInMillis() - startOfTheMonth(monthsAgo).timeInMillis)
                .toInt()

        override fun firstDayOfWeekDaysAgo(weeksAgo: Int): Int =
            TimeUnit.MILLISECONDS.toDays(dayInMillis() - startOfTheWeek(weeksAgo).timeInMillis)
                .toInt()

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

        private fun endOfTheMonth(monthsAgo: Int = 0): Calendar = startOfTheDay().also { calendar ->
            calendar.add(Calendar.MONTH, -monthsAgo)
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        private fun startOfTheWeek(weeksAgo: Int = 0): Calendar = startOfTheDay().also { calendar ->
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            calendar.add(Calendar.DATE, -weeksAgo * 7)
        }

        private fun startOfTheMonth(monthsAgo: Int = 0): Calendar =
            startOfTheDay().also { calendar ->
                calendar.add(Calendar.MONTH, -monthsAgo)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
            }

        private fun month(monthsAgo: Int): Calendar = calendar().also { calendar ->
            calendar.add(Calendar.MONTH, -monthsAgo)
        }
    }
}