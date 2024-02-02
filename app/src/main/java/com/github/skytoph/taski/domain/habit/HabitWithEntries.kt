package com.github.skytoph.taski.domain.habit

import com.github.skytoph.taski.presentation.habit.HabitHistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitWithHistoryUiMapper

data class HabitWithEntries(
    val habit: Habit,
    val entries: EntryList
) {

    fun <T : HabitHistoryUi> map(mapper: HabitWithHistoryUiMapper<T>) = mapper.map(habit, entries)
}