package com.github.skytoph.taski.presentation.habit.edit.mapper

import com.github.skytoph.taski.core.CalendarProvider
import java.io.Serializable
import java.util.Calendar
import java.util.concurrent.TimeUnit

sealed class FrequencyInterval(
    val interval: Int,
    val reschedule: Boolean
) : Serializable {
    abstract fun copy(value: Int): FrequencyInterval
    abstract fun next(timeMillis: Long, targetDay: Int): Long

    class Month(interval: Int, reschedule: Boolean) : FrequencyInterval(interval, reschedule) {
        override fun copy(value: Int): FrequencyInterval =
            Month(interval = interval * value, reschedule = reschedule)

        override fun next(timeMillis: Long, targetDay: Int): Long {
            val calendar = CalendarProvider.getCalendar(false)
            calendar.apply {
                timeInMillis = timeMillis
                repeat(interval) {
                    add(Calendar.DATE, getActualMaximum(Calendar.DAY_OF_MONTH) - get(Calendar.DAY_OF_MONTH) + 1)
                }
                when {
                    targetDay > getActualMaximum(Calendar.DAY_OF_MONTH) -> add(Calendar.DATE, targetDay - 1)
                    else -> set(Calendar.DAY_OF_MONTH, targetDay)
                }
            }
            return calendar.timeInMillis
        }
    }

    class Day(interval: Int, reschedule: Boolean) : FrequencyInterval(interval, reschedule) {
        override fun copy(value: Int): FrequencyInterval =
            Day(interval = interval * value, reschedule = reschedule)

        override fun next(timeMillis: Long, targetDay: Int): Long =
            timeMillis + TimeUnit.DAYS.toMillis(interval.toLong())
    }
}