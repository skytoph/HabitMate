package com.github.skytoph.taski.presentation.habit

import com.github.skytoph.taski.core.Now

interface HabitHistoryUiMapper {
    fun map(history: List<Long>): List<Int>

    class Base(private val now: Now) : HabitHistoryUiMapper {

        override fun map(history: List<Long>): List<Int> {
            return history.map { day ->
                TODO("Not yet implemented")
            }
        }
    }
}