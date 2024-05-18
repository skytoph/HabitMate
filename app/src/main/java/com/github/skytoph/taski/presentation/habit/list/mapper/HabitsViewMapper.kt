package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.view.HabitsView

interface HabitsViewMapper {
    fun map(habits: List<HabitWithEntries>, view: HabitsView): List<HabitWithHistoryUi<HistoryUi>>

    class Base(private val mapper: HabitListUiMapper) : HabitsViewMapper {
        override fun map(habits: List<HabitWithEntries>, view: HabitsView)
                : List<HabitWithHistoryUi<HistoryUi>> {
            val filtered = view.filterBy.item.filter(habits)
            val sorted = view.sortBy.item.sort(habits = filtered, selector = { it.habit })
            return view.viewType.item.map(mapper, sorted)
        }
    }
}