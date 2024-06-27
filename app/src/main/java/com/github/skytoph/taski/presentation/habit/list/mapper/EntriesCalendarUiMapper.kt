package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.presentation.habit.details.HabitStatisticsUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.view.ViewType
import kotlin.math.max

class EntriesCalendarUiMapper(
    private val now: Now,
    private val mapper: EntryUiMapper,
) : HabitHistoryUiMapper<HistoryUi, ViewType.Calendar> {

    override fun map(numberOfColumns: Int, goal: Int, history: EntryList, stats: HabitStatisticsUi): HistoryUi {
        val todayDonePercent =
            ColorPercentMapper.toColorPercent(history.entries[0]?.timesDone ?: 0, goal)
        if (numberOfColumns == 0) return HistoryUi(entries = emptyList(), todayDonePercent)

        val numberOfCells: Int = max(COLUMNS, numberOfColumns) * ROWS

        val todayPosition = 7 - now.dayOfWeek()
        val entries = (numberOfCells - todayPosition - 1 downTo -todayPosition).map { index ->
            mapper.map(history = history, daysAgo = index, goal = goal)
        }
        return HistoryUi(entries = entries, todayDonePercent = todayDonePercent)
    }

    private companion object {
        const val COLUMNS = 50
        const val ROWS = 7
    }
}

class EntriesDailyUiMapper(
    private val mapper: EntryUiMapper,
) : HabitHistoryUiMapper<HistoryUi, ViewType.Daily> {

    override fun map(numberOfEntries: Int, goal: Int, history: EntryList, stats: HabitStatisticsUi): HistoryUi {
        val entries = (0 until numberOfEntries).map { index ->
            mapper.map(history = history, daysAgo = index, goal = goal)
        }
        return HistoryUi(entries = entries)
    }
}