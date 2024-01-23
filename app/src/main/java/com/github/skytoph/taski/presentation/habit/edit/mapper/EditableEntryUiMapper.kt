package com.github.skytoph.taski.presentation.habit.edit.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.list.mapper.ColorPercentMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitEntryUiMapper

class EditableEntryUiMapper(
    private val now: Now,
    private val colorMapper: ColorPercentMapper,
    private val columns: Int = COLUMNS,
) : HabitEntryUiMapper<EntryEditableUi> {

    override fun map(goal: Int, history: Map<Int, Entry>): List<EntryEditableUi> {
        val daysOffset = now.dayOfWeek() - ROWS

        return (0 until columns * ROWS).map { index ->
            val daysAgo = (ROWS - index % ROWS - 1) + index / ROWS * ROWS + daysOffset
            EntryEditableUi(
                now.dayOfMonths(daysAgo).toString(),
                colorMapper.map(history[daysAgo]?.timesDone ?: 0, goal),
                daysAgo
            )
        }
    }

    override fun todayPosition(): Int = 0

    private companion object {
        const val ROWS = 7
        const val COLUMNS = 35
    }
}