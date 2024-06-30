package com.github.skytoph.taski.presentation.habit.edit.mapper

import java.util.Calendar
import java.util.TimeZone

interface HabitDateMapper {
    fun mapEveryday(): List<Calendar>
    fun mapDaily(days: Set<Int>): List<Calendar>
    fun mapMonthly(days: Set<Int>): List<Calendar>
    fun mapCustomDay(timesCount: Int, typeCount: Int): List<Calendar>
    fun mapCustomWeek(timesCount: Int, typeCount: Int): List<Calendar>
    fun mapCustomMonth(timesCount: Int, typeCount: Int): List<Calendar>

    class Base(private val timeZone: TimeZone = TimeZone.getTimeZone("UTC")) : HabitDateMapper {

        override fun mapEveryday(): List<Calendar> = listOf(calendar())

        override fun mapDaily(days: Set<Int>): List<Calendar> = days.map { day ->
            calendar().apply { set(Calendar.DAY_OF_WEEK, dayOfWeekCalendar(day)) }
        }

        override fun mapMonthly(days: Set<Int>): List<Calendar> = days.map { day ->
            calendar().apply { set(Calendar.DAY_OF_MONTH, day) }
        }

        override fun mapCustomDay(timesCount: Int, typeCount: Int): List<Calendar> {
            val step = typeCount / timesCount
            return (0 until timesCount).map {
                calendar().apply { add(Calendar.DAY_OF_YEAR, step * it) }
            }
        }

        override fun mapCustomWeek(timesCount: Int, typeCount: Int): List<Calendar> {
            val step = typeCount * 7 / timesCount
            return (0 until timesCount).map {
                calendar().apply {
                    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                    add(Calendar.DAY_OF_YEAR, step * it)
                }
            }
        }

        override fun mapCustomMonth(timesCount: Int, typeCount: Int): List<Calendar> {
            val step = typeCount * 31 / timesCount
            return (0 until timesCount).map {
                calendar().apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    add(Calendar.DAY_OF_YEAR, step * it)
                }
            }
        }

        private fun dayOfWeekCalendar(day: Int): Int =
            calendar().let { (day + it.firstDayOfWeek + 5) % 7 + 1 }

        private fun calendar(): Calendar = Calendar.getInstance(timeZone)
    }
}

interface MapToDates {
    fun dates(mapper: HabitDateMapper): List<Calendar>
}

interface MapToDatesCustom {
    fun dates(mapper: HabitDateMapper, timesCount: Int, typeCount: Int): List<Calendar>
}