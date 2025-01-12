package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.details.HabitStatisticsUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi

class EditableEntryCalendarUiMapper(
    private val now: Now,
    private val entryMapper: EditableEntryDomainToUiMapper
) : AbstractEditableEntryUiMapper(now) {

    override fun initialize(isFirstDaySunday: Boolean, page: Int) {
        weeksAgoStart = page
        weeks = 0
    }

    override fun weeksInMonth(isFirstDaySunday: Boolean, monthsAgo: Int): Int =
        (now.firstDayOfMonthDaysAgo(monthsAgo) - now.lastDayOfMonthDaysAgo(monthsAgo)).div(ROWS)

    override fun entries(
        monthsAgo: Int, weeks: Int, history: Map<Int, Entry>, goal: Int,
        stats: HabitStatisticsUi, isBorderOn: Boolean, isFirstDaySunday: Boolean
    ): Map<Int, EntryEditableUi> {
        val firstDay = now.firstDayOfMonthDaysAgo(monthsAgo)
        val firstDayOfWeek = firstDay - 1 + now.dayOfWeek(isFirstDaySunday = isFirstDaySunday, daysAgo = firstDay)
        val lastDay = now.lastDayOfMonthDaysAgo(monthsAgo)
        val lastDayOfWeek = lastDay - ROWS + now.dayOfWeek(isFirstDaySunday = isFirstDaySunday, daysAgo = lastDay)
        return (firstDayOfWeek downTo lastDayOfWeek).associateWith { daysAgo ->
            val timesDone = history[daysAgo]?.timesDone ?: 0
            val hasBorder = isBorderOn && timesDone == 0 && stats.isInRange(daysAgo)
            val isDisabled = daysAgo > firstDay || daysAgo < lastDay
            entryMapper.map(daysAgo, timesDone, goal, hasBorder, isDisabled)
        }
    }
}