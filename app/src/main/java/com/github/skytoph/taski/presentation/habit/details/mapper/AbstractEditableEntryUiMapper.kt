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

abstract class AbstractEditableEntryUiMapper(
    private val now: Now,
) : HabitHistoryUiMapper<EditableHistoryUi, ViewType> {

    protected var weeksAgoStart: Int = 0
    protected var weeks: Int = 0

    abstract fun initialize(isFirstDaySunday: Boolean, page: Int)

    abstract fun weeksInMonth(isFirstDaySunday: Boolean, weeksAgo: Int): Int

    override fun map(
        page: Int, goal: Int, history: EntryList, stats: HabitStatisticsUi,
        isBorderOn: Boolean, isFirstDaySunday: Boolean
    ): EditableHistoryUi {
        initialize(isFirstDaySunday, page)
        return EditableHistoryUi(
            entries(
                weeksAgo = weeksAgoStart,
                weeks = weeks,
                history = history.entries,
                goal = goal,
                stats = stats,
                isBorderOn = isBorderOn,
                isFirstDaySunday = isFirstDaySunday
            ), month(page, weeks)
        )
    }

    abstract fun entries(
        weeksAgo: Int, weeks: Int, history: Map<Int, Entry>, goal: Int,
        stats: HabitStatisticsUi, isBorderOn: Boolean, isFirstDaySunday: Boolean
    ): Map<Int, EntryEditableUi>

    private fun month(monthsAgo: Int, weeks: Int): MonthUi =
        MonthUi(now.monthMillis(monthsAgo), weeks, now.numberOfMonth(monthsAgo))

    protected companion object {
        const val ROWS = 7
    }
}