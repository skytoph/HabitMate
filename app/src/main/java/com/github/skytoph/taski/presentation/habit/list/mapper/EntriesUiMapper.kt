package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.list.EntryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi

class EntriesUiMapper(
    private val now: Now,
    private val numberOfCells: Int = COLUMNS * ROWS
) : HabitHistoryUiMapper<HistoryUi> {

    override fun map(goal: Int, history: Map<Int, Entry>): HistoryUi {
        val todayPosition = 7 - now.dayOfWeek()
        val entries =  (numberOfCells - todayPosition - 1 downTo -todayPosition).map { index ->
            val timesDone = history[index]?.timesDone ?: 0
            val colorPercent = ColorPercentMapper.toColorPercent(timesDone, goal)
            EntryUi(colorPercent = colorPercent)
        }
        return HistoryUi(entries = entries, todayPosition = todayPosition())
    }

    private fun todayPosition(): Int = numberOfCells - 8 + now.dayOfWeek()

    private companion object {
        const val COLUMNS = 50
        const val ROWS = 7
    }
}