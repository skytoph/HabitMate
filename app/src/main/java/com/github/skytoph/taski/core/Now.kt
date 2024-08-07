package com.github.skytoph.taski.core

import java.util.Calendar
import java.util.concurrent.TimeUnit

interface Now {
    fun dayOfWeek(isFirstDaySunday: Boolean): Int
    fun dayOfWeek(isFirstDaySunday: Boolean, daysAgo: Int): Int
    fun lastDayOfWeekDate(isFirstDaySunday: Boolean, weeksAgo: Int = 0): Int
    fun lastDayOfWeekMillis(isFirstDaySunday: Boolean, weeksAgo: Int = 0): Long
    fun lastDayOfWeekDaysAgo(isFirstDaySunday: Boolean, weeksAgo: Int): Int
    fun firstDayOfWeekDaysAgo(isFirstDaySunday: Boolean, weeksAgo: Int): Int
    fun dayOfMonths(daysAgo: Int): Int
    fun milliseconds(): Long
    fun daysAgo(milliseconds: Long): Int
    fun daysAgoInMillis(days: Int): Long
    fun dayInMillis(): Long
    fun monthMillis(monthsAgo: Int = 0): Long
    fun daysInMonth(daysAgo: Int): Int
    fun monthsAgo(daysAgo: Int): Int
    fun lastDayOfMonthDaysAgo(monthsAgo: Int): Int
    fun firstDayOfMonthDaysAgo(monthsAgo: Int): Int
    fun numberOfMonth(monthsAgo: Int): Int

    val default: Boolean

    class Base(override val default: Boolean = false) : Now {

        override fun monthsAgo(daysAgo: Int): Int {
            val compareWith = calendar().apply { add(Calendar.DAY_OF_YEAR, -daysAgo) }
            val today = calendar()
            return 12 * (today.get(Calendar.YEAR) - compareWith.get(Calendar.YEAR)) +
                    today.get(Calendar.MONTH) - compareWith.get(Calendar.MONTH)
        }

        override fun dayOfWeek(isFirstDaySunday: Boolean): Int =
            calendar(isFirstDaySunday).run { (get(Calendar.DAY_OF_WEEK) - firstDayOfWeek + 7) % 7 } + 1

        override fun dayOfWeek(isFirstDaySunday: Boolean, daysAgo: Int): Int =
            (6 + dayOfWeek(isFirstDaySunday) - daysAgo % 7) % 7 + 1

        override fun milliseconds(): Long = System.currentTimeMillis()

        override fun dayOfMonths(daysAgo: Int): Int =
            startOfTheDay(daysAgo, calendar()).get(Calendar.DAY_OF_MONTH)

        override fun daysInMonth(daysAgo: Int): Int =
            startOfTheDay(daysAgo, calendar()).getActualMaximum(Calendar.DAY_OF_MONTH)

        override fun daysAgoInMillis(days: Int): Long = startOfTheDay(days, calendar()).timeInMillis

        override fun daysAgo(milliseconds: Long): Int =
            TimeUnit.MILLISECONDS.toDays(dayInMillis() - milliseconds).toInt()

        override fun dayInMillis(): Long = startOfTheDay(calendar = calendar()).timeInMillis

        override fun lastDayOfWeekDate(isFirstDaySunday: Boolean, weeksAgo: Int): Int =
            endOfTheWeek(isFirstDaySunday, weeksAgo).get(Calendar.DAY_OF_MONTH)

        override fun lastDayOfWeekDaysAgo(isFirstDaySunday: Boolean, weeksAgo: Int): Int =
            TimeUnit.MILLISECONDS.toDays(dayInMillis() - endOfTheWeek(isFirstDaySunday, weeksAgo).timeInMillis)
                .toInt()

        override fun firstDayOfWeekDaysAgo(isFirstDaySunday: Boolean, weeksAgo: Int): Int =
            TimeUnit.MILLISECONDS.toDays(dayInMillis() - startOfTheWeek(isFirstDaySunday, weeksAgo).timeInMillis)
                .toInt()

        override fun lastDayOfWeekMillis(isFirstDaySunday: Boolean, weeksAgo: Int): Long =
            endOfTheWeek(isFirstDaySunday, weeksAgo).timeInMillis

        private fun endOfTheWeek(isFirstDaySunday: Boolean, weeksAgo: Int = 0): Calendar =
            startOfTheDay(calendar = calendar(isFirstDaySunday)).also { calendar ->
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.add(Calendar.DATE, 6 - weeksAgo * 7)
            }

        private fun startOfTheWeek(isFirstDaySunday: Boolean, weeksAgo: Int = 0): Calendar =
            startOfTheDay(calendar = calendar(isFirstDaySunday)).also { calendar ->
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.add(Calendar.DATE, -weeksAgo * 7)
            }

        override fun lastDayOfMonthDaysAgo(monthsAgo: Int): Int =
            TimeUnit.MILLISECONDS.toDays(dayInMillis() - endOfTheMonth(monthsAgo).timeInMillis).toInt()

        override fun firstDayOfMonthDaysAgo(monthsAgo: Int): Int =
            TimeUnit.MILLISECONDS.toDays(dayInMillis() - startOfTheMonth(monthsAgo).timeInMillis).toInt()

        override fun monthMillis(monthsAgo: Int): Long = month(monthsAgo).timeInMillis

        override fun numberOfMonth(monthsAgo: Int): Int = month(monthsAgo).get(Calendar.MONTH)

        private fun startOfTheDay(daysAgo: Int = 0, calendar: Calendar = calendar()): Calendar = calendar.also {
            it.set(Calendar.HOUR_OF_DAY, 0)
            it.set(Calendar.MINUTE, 0)
            it.set(Calendar.SECOND, 0)
            it.set(Calendar.MILLISECOND, 0)
            it.add(Calendar.DAY_OF_YEAR, -daysAgo)
        }

        private fun endOfTheMonth(monthsAgo: Int = 0): Calendar =
            startOfTheDay(calendar = calendar()).also { calendar ->
                calendar.add(Calendar.MONTH, -monthsAgo)
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            }

        private fun startOfTheMonth(monthsAgo: Int = 0): Calendar =
            startOfTheDay(calendar = calendar()).also { calendar ->
                calendar.add(Calendar.MONTH, -monthsAgo)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
            }

        private fun month(monthsAgo: Int): Calendar = calendar().also { calendar ->
            calendar.add(Calendar.MONTH, -monthsAgo)
        }

        private fun calendar(isFirstDaySunday: Boolean = default): Calendar =
            CalendarProvider.getCalendarDefault(isFirstDaySunday)
    }
}