package com.github.skytoph.taski.domain.habit

import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.view.ViewType
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitWithHistoryUiMapper

data class HabitWithEntries(
    val habit: Habit,
    val entries: EntryList
) {

    fun <V : ViewType> map(
        mapper: HabitWithHistoryUiMapper<HistoryUi, V>, numberOfEntries: Int = 0
    ): HabitWithHistoryUi<HistoryUi> = mapper.map(habit, entries, numberOfEntries)
}