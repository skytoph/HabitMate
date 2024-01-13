package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.core.Now

interface HabitHistoryUiMapper {
    fun map(history: List<Long>): List<Int>

    class Base(private val now: Now) : HabitHistoryUiMapper {

        override fun map(history: List<Long>): List<Int> {
            return history.map { day ->
                //todo replace with implementation
                day.toInt()
            }
        }
    }
}