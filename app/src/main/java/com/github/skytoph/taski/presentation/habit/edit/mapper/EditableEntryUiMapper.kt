package com.github.skytoph.taski.presentation.habit.edit.mapper

import androidx.compose.ui.text.style.TextAlign
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.edit.MonthUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper

class EditableEntryUiMapper(private val now: Now) : HabitHistoryUiMapper<EditableHistoryUi> {

    override fun map(column: Int, goal: Int, history: EntryList): EditableHistoryUi {
        return EditableHistoryUi(entries(column, history.entries), months())
    }

    private fun entries(column: Int, history: Map<Int, Entry>): List<EntryEditableUi> =
        (0 until COLUMNS * ROWS).map { index ->
            val daysAgo =
                now.dayOfWeek() - index % ROWS - now.firstDayOfWeek() + index / ROWS * ROWS
            val timesDone = history[daysAgo]?.timesDone ?: 0
            EntryEditableUi(
                day = now.dayOfMonths(daysAgo).toString(),
                timesDone = timesDone,
                daysAgo = daysAgo
            )
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