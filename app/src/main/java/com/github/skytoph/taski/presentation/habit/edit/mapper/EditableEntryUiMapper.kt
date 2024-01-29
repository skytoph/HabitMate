package com.github.skytoph.taski.presentation.habit.edit.mapper

import androidx.compose.ui.text.style.TextAlign
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.edit.MonthUi
import com.github.skytoph.taski.presentation.habit.list.mapper.ColorPercentMapper
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper

class EditableEntryUiMapper(
    private val now: Now,
    private val colorMapper: ColorPercentMapper,
    private val columns: Int = COLUMNS,
) : HabitHistoryUiMapper<EditableHistoryUi> {

    override fun map(goal: Int, history: Map<Int, Entry>): EditableHistoryUi {
        return EditableHistoryUi(entries(goal, history), months())
    }

    private fun entries(goal: Int, history: Map<Int, Entry>): List<EntryEditableUi> {
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

    private fun months(): List<MonthUi> {
        val months: MutableList<MonthUi> = ArrayList()

        var weeks = 0
        for (index in 0 until COLUMNS) {
            val day = now.lastDayOfWeekDate(index)
            if (day < 8) {
                months.add(MonthUi(now.lastDayOfWeekMillis(index), weeks + 1))
                weeks = 0
            } else {
                weeks++
            }
        }
        if (weeks > 0)
            months.add(MonthUi(now.lastDayOfWeekMillis(COLUMNS - 1), weeks))

        if (months[0].weeks == 1) {
            months[0] = months[0].copy(weeks = 2, alignment = TextAlign.End)
            months[1] = months[1].copy(weeks = months[1].weeks - 1)
        }
        if (months[months.size - 1].weeks == 1) {
            months[months.size - 1] = months[months.size - 1].copy(weeks = 2)
            months[months.size - 2] =
                months[months.size - 2].copy(weeks = months[months.size - 2].weeks - 1)
        }
        return months
    }

    private companion object {
        const val ROWS = 7
        const val COLUMNS = 37
    }
}