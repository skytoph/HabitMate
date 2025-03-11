package com.skytoph.taski.presentation.habit.list.mapper

import com.skytoph.taski.core.datastore.settings.FilterHabits
import com.skytoph.taski.core.datastore.settings.HabitsView
import com.skytoph.taski.domain.habit.Habit
import com.skytoph.taski.domain.habit.HabitWithEntries
import com.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper
import com.skytoph.taski.presentation.habit.list.HistoryUi

interface HabitsViewMapper {
    fun map(habits: List<HabitWithEntries>, view: HabitsView, isBorderOn: Boolean, isFirstDaySunday: Boolean)
            : List<HabitWithHistoryUi<HistoryUi>>

    class Base(private val mapper: HabitListUiMapper, private val streakMapper: StatisticsUiMapper) : HabitsViewMapper {
        override fun map(
            habits: List<HabitWithEntries>, view: HabitsView, isBorderOn: Boolean, isFirstDaySunday: Boolean
        ): List<HabitWithHistoryUi<HistoryUi>> {
            val selector: (HabitWithEntries) -> Habit = { it.habit }
            val selectorSort: (HabitWithEntries) -> Pair<Int, Habit> =
                { (it.entries.entries[0]?.timesDone ?: 0) to it.habit }
            val today: (HabitWithEntries) -> Int = { it.entries.entries[0]?.timesDone ?: 0 }

            val todayOnly = FilterHabits.Today(view.showTodayHabitsOnly)
                .filter(habits = habits, isFirstDaySunday = isFirstDaySunday, mapper = streakMapper)
            val notArchived = FilterHabits.Archived().filter(habits = todayOnly, selector = selector)
            val filtered = view.filterBy.filter(habits = notArchived, selector = selector, today = today)
            val sorted = view.sortBy.sort(habits = filtered, selector = selectorSort)
            return view.viewType.map(mapper, sorted, isBorderOn, isFirstDaySunday)
        }
    }
}