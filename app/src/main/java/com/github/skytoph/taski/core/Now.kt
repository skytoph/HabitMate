package com.github.skytoph.taski.core

import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit

interface Now {
    fun dayOfWeek(): Int
    fun dayOfMonths(daysAgo: Int): Int
    fun milliseconds(): Long
    fun daysAgo(milliseconds: Long): Int
    fun daysAgoInMillis(days: Int): Long
    fun dayInMillis(): Long

    class Base : Now {

        override fun dayOfWeek(): Int = calendar().get(Calendar.DAY_OF_WEEK)

        override fun milliseconds(): Long = System.currentTimeMillis()

        override fun daysAgoInMillis(days: Int): Long = startOfTheDay(days).timeInMillis

        override fun dayOfMonths(daysAgo: Int): Int =
            startOfTheDay(daysAgo).get(Calendar.DAY_OF_MONTH)

        override fun daysAgo(milliseconds: Long): Int =
            TimeUnit.MILLISECONDS.toDays(dayInMillis() - milliseconds).toInt()

        override fun dayInMillis(): Long = startOfTheDay(0).timeInMillis

        private fun startOfTheDay(daysAgo: Int): Calendar = calendar().also {
            it.set(Calendar.HOUR_OF_DAY, 0)
            it.set(Calendar.MINUTE, 0)
            it.set(Calendar.SECOND, 0)
            it.set(Calendar.MILLISECOND, 0)
            it.add(Calendar.DAY_OF_YEAR, -daysAgo)
        }

        private fun calendar(): Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    }
}