package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.list.EntryUi

class EntriesUiMapper(
    private val now: Now,
    private val colorMapper: ColorPercentMapper,
    private val numberOfCells: Int = COLUMNS * ROWS
) : HabitEntryUiMapper<EntryUi> {

    override fun map(goal: Int, history: Map<Int, Entry>): List<EntryUi> {
        val todayPosition = 7 - now.dayOfWeek()
        return (numberOfCells - todayPosition - 1 downTo -todayPosition).map { index ->
            val timesDone = history[index]?.timesDone ?: 0
            val colorPercent = colorMapper.map(timesDone, goal)
            EntryUi(colorPercent = colorPercent)
        }
    }

    override fun todayPosition(): Int = numberOfCells - 8 + now.dayOfWeek()

    private companion object {
        const val COLUMNS = 50
        const val ROWS = 7
    }
}