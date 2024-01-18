package com.github.skytoph.taski.core

import java.util.Calendar
import java.util.TimeZone

interface Now {
    fun dayOfWeek(): Int
    fun milliseconds(): Long
    fun daysAgoInMillis(days: Int): Long
    fun dayInMillis(): Long

    class Base : Now {

        private fun calendar(): Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        override fun dayOfWeek(): Int = calendar().get(Calendar.DAY_OF_WEEK)

        override fun milliseconds(): Long = System.currentTimeMillis()

        override fun daysAgoInMillis(days: Int): Long {
            val calendar = calendar()
            calendar.add(Calendar.DAY_OF_YEAR, days)
            return calendar.timeInMillis
        }

        override fun dayInMillis(): Long = calendar().also {
            it.set(Calendar.HOUR_OF_DAY, 0)
            it.set(Calendar.MINUTE, 0)
            it.set(Calendar.SECOND, 0)
            it.set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}