package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.view.FilterHabits
import com.github.skytoph.taski.presentation.habit.list.view.HabitsView

interface HabitsViewMapper {
    fun map(habits: List<HabitWithEntries>, view: HabitsView, isBorderOn: Boolean, isFirstDaySunday: Boolean)
            : List<HabitWithHistoryUi<HistoryUi>>

    class Base(private val mapper: HabitListUiMapper) : HabitsViewMapper {
        override fun map(
            habits: List<HabitWithEntries>, view: HabitsView, isBorderOn: Boolean, isFirstDaySunday: Boolean
        ): List<HabitWithHistoryUi<HistoryUi>> {
            val selector: (HabitWithEntries) -> Habit = { it.habit }
            val today: (HabitWithEntries) -> Int = { it.entries.entries[0]?.timesDone ?: 0 }
            val notArchived = FilterHabits.Archived().filter(habits = habits, selector = selector)
            val filtered = view.filterBy
                .filter(habits = notArchived, selector = selector, today = today)
            val sorted = view.sortBy.sort(habits = filtered, selector = selector)
            return view.viewType.map(mapper, sorted, isBorderOn, isFirstDaySunday)
        }
    }
}