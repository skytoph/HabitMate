package com.github.skytoph.taski.presentation.habit.edit.mapper

import java.util.Calendar
import java.util.TimeZone

interface HabitDateMapper {
    fun mapEveryday(): Map<Int, Calendar>
    fun mapDaily(days: Set<Int>): Map<Int, Calendar>
    fun mapMonthly(days: Set<Int>): Map<Int, Calendar>
    fun mapCustomDay(timesCount: Int, typeCount: Int): Map<Int, Calendar>
    fun mapCustomWeek(timesCount: Int, typeCount: Int): Map<Int, Calendar>
    fun mapCustomMonth(timesCount: Int, typeCount: Int): Map<Int, Calendar>

    class Base(private val timeZone: TimeZone = TimeZone.getDefault()) : HabitDateMapper {

        override fun mapEveryday(): Map<Int, Calendar> = mapOf(1 to calendar())

        override fun mapDaily(days: Set<Int>): Map<Int, Calendar> = days.associateWith { day ->
            calendar().apply { set(Calendar.DAY_OF_WEEK, dayOfWeekCalendar(day)) }
        }

        override fun mapMonthly(days: Set<Int>): Map<Int, Calendar> = days.associateWith { day ->
            calendar().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                add(Calendar.DAY_OF_YEAR, day - 1)
            }
        }

        override fun mapCustomDay(timesCount: Int, typeCount: Int): Map<Int, Calendar> {
            val step = typeCount / timesCount
            return (0 until timesCount).associateWith {
                calendar().apply { add(Calendar.DAY_OF_YEAR, step * it) }
            }
        }

        override fun mapCustomWeek(timesCount: Int, typeCount: Int): Map<Int, Calendar> {
            val step = typeCount * 7 / timesCount
            return (0 until timesCount).associate {
                val day = step * it
                day to calendar().apply {
                    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                    add(Calendar.DAY_OF_YEAR, day)
                }
            }
        }

        override fun mapCustomMonth(timesCount: Int, typeCount: Int): Map<Int, Calendar> {
            val step = (typeCount * 28 / timesCount).let { if (it == 0) 1 else it }
            return (0 until timesCount).associate {
                val day = step * it
                day to calendar().apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    add(Calendar.DAY_OF_YEAR, day)
                }
            }
        }

        private fun dayOfWeekCalendar(day: Int): Int =
            calendar().let { (day + it.firstDayOfWeek + 5) % 7 + 1 }

        private fun calendar(): Calendar = Calendar.getInstance(timeZone)
    }
}

interface MapToDates {
    fun dates(mapper: HabitDateMapper): Map<Int, Calendar>
}

interface MapToDatesCustom {
    fun dates(mapper: HabitDateMapper, timesCount: Int, typeCount: Int): Map<Int, Calendar>
}