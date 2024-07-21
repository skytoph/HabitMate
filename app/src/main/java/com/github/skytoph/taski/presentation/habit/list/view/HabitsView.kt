package com.github.skytoph.taski.presentation.habit.list.view

import com.github.skytoph.taski.core.datastore.Settings
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitsViewMapper

data class HabitsView(
    val viewType: ViewType = ViewType.Calendar(),
    val sortBy: SortHabits = SortHabits.ByTitle,
    val filterBy: FilterHabits = FilterHabits.None,
    val showTodayHabitsOnly: Boolean = false,
    val initialized: Boolean = false
) {
    fun map(mapper: HabitsViewMapper, habits: List<HabitWithEntries>, settings: Settings)
            : List<HabitWithHistoryUi<HistoryUi>>? =
        if (!initialized) null
        else mapper.map(habits, this, settings.currentDayHighlighted, settings.weekStartsOnSunday.value)
}