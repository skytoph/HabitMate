package com.github.skytoph.taski.presentation.habit.details.mapper

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.MonthUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitHistoryUiMapper
import kotlin.math.ceil

class EditableEntryUiMapper(
    private val now: Now,
    private val cache: WeeksCache,
    private val entryMapper: EditableEntryDomainToUiMapper
) : HabitHistoryUiMapper<EditableHistoryUi> {

    override fun map(page: Int, goal: Int, history: EntryList): EditableHistoryUi {
        val weeksAgoStart = cache.get()
        val weeks = weeksInMonth(weeksAgoStart)
        cache.add(weeks)
        return EditableHistoryUi(entries(weeksAgoStart, weeks, history.entries), month(page, weeks))
    }

    private fun weeksInMonth(weeksAgo: Int): Int =
        ceil(now.lastDayOfWeekDate(weeksAgo).toFloat().div(ROWS)).toInt()
            .let { if (it > 0) it else if (weeksAgo == 0) 2 else weeksInMonth(weeksAgo + 1) }

    private fun entries(weeksAgo: Int, weeks: Int, history: Map<Int, Entry>) =
        (weeksAgo * ROWS until weeksAgo * ROWS + weeks * ROWS).map { index ->
            val daysAgo =
                now.dayOfWeek() - index % ROWS - now.firstDayOfWeek() + index / ROWS * ROWS
            val timesDone = history[daysAgo]?.timesDone ?: 0
            entryMapper.map(daysAgo, timesDone)
        }

    private fun month(monthsAgo: Int, weeks: Int): MonthUi =
        MonthUi(now.monthMillis(monthsAgo), weeks)

    private companion object {
        const val ROWS = 7
    }
}

class WeeksCache(private var weeks: Int = 0) {

    fun add(weeks: Int) {
        this.weeks += weeks
    }

    fun get(): Int = weeks
}