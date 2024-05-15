package com.github.skytoph.taski.presentation.habit.list.view

import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitsViewMapper

data class HabitsView(
    val viewType: ViewOption = HabitsViewOptionsProvider.optionCalendar,
    val sortBy: SortOption = HabitsViewOptionsProvider.optionSortByTitle,
    val filterBy: FilterOption = HabitsViewOptionsProvider.optionFilterNone
) {
    fun map(mapper: HabitsViewMapper, habits: List<HabitWithEntries>)
            : List<HabitWithHistoryUi<HistoryUi>> = mapper.map(habits, this)
}