package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.details.HabitStatisticsUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import kotlin.math.ceil

class EditableEntryGridUiMapper(
    private val now: Now,
    private val cache: WeeksCache,
    private val entryMapper: EditableEntryDomainToUiMapper
) : AbstractEditableEntryUiMapper(now) {

    override fun initialize(isFirstDaySunday: Boolean, page: Int) {
        weeksAgoStart = cache.get()
        weeks = weeksInMonth(isFirstDaySunday, weeksAgoStart)
        cache.add(weeks)
    }

    override fun entries(
        weeksAgo: Int, weeks: Int, history: Map<Int, Entry>, goal: Int,
        stats: HabitStatisticsUi, isBorderOn: Boolean, isFirstDaySunday: Boolean
    ): Map<Int, EntryEditableUi> =
        (weeksAgo * ROWS until weeksAgo * ROWS + weeks * ROWS).associate { index ->
            val daysAgo = now.dayOfWeek(isFirstDaySunday) - index % ROWS + index / ROWS * ROWS - 1
            val timesDone = history[daysAgo]?.timesDone ?: 0
            val streakType = if (isBorderOn && timesDone == 0) stats.type(daysAgo) else null
            val isDisabled = daysAgo < 0
            daysAgo to entryMapper.map(daysAgo, timesDone, goal, streakType, isDisabled)
        }

    override fun weeksInMonth(isFirstDaySunday: Boolean, weeksAgo: Int): Int =
        ceil(now.lastDayOfWeekDate(isFirstDaySunday, weeksAgo).toFloat().div(ROWS)).toInt().let {
            if (it > 0) it
            else if (weeksAgo == 0) 2
            else weeksInMonth(isFirstDaySunday, weeksAgo + 1)
        }

    fun clear() {
        cache.clear()
    }
}