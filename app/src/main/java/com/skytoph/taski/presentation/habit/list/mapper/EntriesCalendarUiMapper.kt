package com.skytoph.taski.presentation.habit.list.mapper

import com.skytoph.taski.core.Now
import com.skytoph.taski.core.datastore.settings.ViewType
import com.skytoph.taski.domain.habit.Entry
import com.skytoph.taski.domain.habit.EntryList
import com.skytoph.taski.presentation.habit.details.HabitStatisticsUi
import com.skytoph.taski.presentation.habit.list.EntryUi
import com.skytoph.taski.presentation.habit.list.HistoryUi
import kotlin.math.max

class EntriesCalendarUiMapper(
    private val now: Now,
    private val mapper: EntryUiMapper,
) : HabitHistoryUiMapper<HistoryUi, ViewType.Calendar> {

    override fun map(
        numberOfColumns: Int,
        goal: Int,
        history: EntryList,
        stats: HabitStatisticsUi,
        isBorderOn: Boolean,
        isFirstDaySunday: Boolean
    ): HistoryUi {
        val timesDone = history.entries[0]?.timesDone ?: 0
        val todayDonePercent = ColorPercentMapper.toColorPercent(timesDone, goal)
        if (numberOfColumns == 0) return HistoryUi(todayDonePercent = todayDonePercent)

        val numberOfCells: Int = max(COLUMNS, numberOfColumns) * ROWS

        val todayPosition = 7 - now.dayOfWeek(isFirstDaySunday)
        val entries = history.entries.mapValues { (daysAgo, entry) ->
            mapper.map(entry = entry, daysAgo = daysAgo, goal = goal, isBorderOn = isBorderOn)
        }.let {
            if (!it.containsKey(0))
                it.toMutableMap().apply { put(0, EntryUi(daysAgo = 0, hasBorder = isBorderOn)) }
            else it
        }
        return HistoryUi(
            entries = entries,
            todayDonePercent = todayDonePercent,
            todayDone = timesDone,
            fromDaysAgo = -todayPosition,
            toDaysAgo = numberOfCells - todayPosition - 1
        )
    }

    private companion object {
        const val COLUMNS = 50
        const val ROWS = 7
    }
}

class EntriesDailyUiMapper(
    private val mapper: EntryUiMapper,
) : HabitHistoryUiMapper<HistoryUi, ViewType.Daily> {

    override fun map(
        numberOfEntries: Int,
        goal: Int,
        history: EntryList,
        stats: HabitStatisticsUi,
        isBorderOn: Boolean,
        isFirstDaySunday: Boolean
    ): HistoryUi {
        val entries = (0 until numberOfEntries).associateWith { index ->
            val entry: Entry? = history.entries[index]
            mapper.map(entry = entry, daysAgo = index, goal = goal, isBorderOn = isBorderOn)
        }
        return HistoryUi(entries = entries)
    }
}