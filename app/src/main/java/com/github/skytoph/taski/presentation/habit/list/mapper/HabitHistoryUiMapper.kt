package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.HabitUi
import java.util.concurrent.TimeUnit

interface HabitHistoryUiMapper {
    fun map(history: List<Entry>): Map<Int, Int>
    fun todayPosition(): Int

    class Base(private val now: Now) : HabitHistoryUiMapper {

        override fun map(history: List<Entry>): Map<Int, Int> {
            val currentIndex = todayPosition()
            return history.associateBy(
                { timestampToIndex(it.timestamp, currentIndex) },
                { it.timesDone })
        }

        override fun todayPosition(): Int = HabitUi.MAX_DAYS + now.dayOfWeek() - 8

        private fun timestampToIndex(timestamp: Long, currentIndex: Int): Int {
            val daysAgo = TimeUnit.MILLISECONDS.toDays(now.dayInMillis() - timestamp).toInt()
            return currentIndex - daysAgo
        }
    }
}