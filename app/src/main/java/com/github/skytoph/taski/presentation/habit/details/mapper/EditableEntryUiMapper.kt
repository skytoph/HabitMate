package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.presentation.habit.details.HabitStatisticsUi
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.edit.MonthUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import com.github.skytoph.taski.presentation.habit.list.view.ViewType
import kotlin.math.ceil

class EditableEntryUiMapper(
    private val now: Now,
    private val cache: WeeksCache,
    private val entryMapper: EditableEntryDomainToUiMapper
) : HabitHistoryUiMapper<EditableHistoryUi, ViewType> {

    override fun map(
        page: Int, goal: Int, history: EntryList, stats: HabitStatisticsUi,
        isBorderOn: Boolean, isFirstDaySunday: Boolean
    ): EditableHistoryUi {
        val weeksAgoStart = cache.get()
        val weeks = weeksInMonth(isFirstDaySunday, weeksAgoStart)
        cache.add(weeks)
        return EditableHistoryUi(
            entries(weeksAgoStart, weeks, history.entries, goal, stats, isBorderOn, isFirstDaySunday),
            month(page, weeks)
        )
    }

    private fun weeksInMonth(isFirstDaySunday: Boolean, weeksAgo: Int): Int =
        ceil(now.lastDayOfWeekDate(isFirstDaySunday).toFloat().div(ROWS)).toInt()
            .let { if (it > 0) it else if (weeksAgo == 0) 2 else weeksInMonth(isFirstDaySunday, weeksAgo + 1) }

    private fun entries(
        weeksAgo: Int, weeks: Int, history: Map<Int, Entry>, goal: Int,
        stats: HabitStatisticsUi, isBorderOn: Boolean, isFirstDaySunday: Boolean
    ): Map<Int, EntryEditableUi> =
        (weeksAgo * ROWS until weeksAgo * ROWS + weeks * ROWS).associate { index ->
            val daysAgo =
                now.dayOfWeek(isFirstDaySunday) - index % ROWS + index / ROWS * ROWS - 1
            val timesDone = history[daysAgo]?.timesDone ?: 0
            val hasBorder = isBorderOn && timesDone == 0 && stats.isInRange(daysAgo)
            daysAgo to entryMapper.map(daysAgo, timesDone, goal, hasBorder)
        }

    private fun month(monthsAgo: Int, weeks: Int): MonthUi =
        MonthUi(now.monthMillis(monthsAgo), weeks, now.numberOfMonth(monthsAgo))

    private companion object {
        const val ROWS = 7
    }
}