package com.github.skytoph.taski.domain.habit

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitWithHistoryUiMapper

data class HabitWithEntries(
    val habit: Habit,
    val entries: EntryList
) {

    fun map(
        mapper: HabitWithHistoryUiMapper<HistoryUi>, defaultColor: Color
    ): HabitWithHistoryUi<HistoryUi> = mapper.map(habit, entries, defaultColor)
}