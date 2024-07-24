package com.github.skytoph.taski.presentation.habit.edit.mapper

import com.github.skytoph.taski.core.CalendarProvider
import java.util.Calendar

interface HabitDateMapper {
    fun mapEveryday(): Map<Int, Calendar>
    fun mapDaily(isFirstDaySunday: Boolean, days: Set<Int>): Map<Int, Calendar>
    fun mapMonthly(days: Set<Int>): Map<Int, Calendar>
    fun mapCustomDay(timesCount: Int, typeCount: Int): Map<Int, Calendar>
    fun mapCustomWeek(isFirstDaySunday: Boolean, timesCount: Int, typeCount: Int): Map<Int, Calendar>
    fun mapCustomMonth(timesCount: Int, typeCount: Int): Map<Int, Calendar>

    class Base : HabitDateMapper {

        override fun mapEveryday(): Map<Int, Calendar> = mapOf(1 to calendar())

        override fun mapDaily(isFirstDaySunday: Boolean, days: Set<Int>): Map<Int, Calendar> =
            days.associateWith { day ->
                calendar().apply { set(Calendar.DAY_OF_WEEK, day) }
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

        override fun mapCustomWeek(isFirstDaySunday: Boolean, timesCount: Int, typeCount: Int): Map<Int, Calendar> {
            val step = typeCount * 7 / timesCount
            return (0 until timesCount).associate {
                val day = step * it
                day to calendar(isFirstDaySunday).apply {
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

        private fun calendar(isFirstDaySunday: Boolean = false): Calendar =
            CalendarProvider.getCalendar(isFirstDaySunday)
    }
}

interface MapToDates {
    fun dates(mapper: HabitDateMapper, isFirstDaySunday: Boolean): Map<Int, Calendar>

    class MapDaily(private val days: Set<Int>) : MapToDates {
        override fun dates(mapper: HabitDateMapper, isFirstDaySunday: Boolean): Map<Int, Calendar> =
            mapper.mapDaily(isFirstDaySunday, days)
    }
}

interface MapToDatesCustom {
    fun dates(mapper: HabitDateMapper, isFirstDaySunday: Boolean, timesCount: Int, typeCount: Int): Map<Int, Calendar>
}