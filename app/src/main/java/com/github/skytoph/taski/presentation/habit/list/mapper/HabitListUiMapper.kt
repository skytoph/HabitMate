package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HabitsView
import com.github.skytoph.taski.presentation.habit.list.HistoryUi

interface HabitListUiMapper {
    fun mapCalendar(habits: List<HabitWithEntries>, columnsNumber: Int)
            : List<HabitWithHistoryUi<HistoryUi>>

    fun mapDaily(habits: List<HabitWithEntries>, entriesNumber: Int)
            : List<HabitWithHistoryUi<HistoryUi>>

    class Base(
        private val mapperDaily: HabitWithHistoryUiMapper<HistoryUi, HabitsView.Daily>,
        private val mapperCalendar: HabitWithHistoryUiMapper<HistoryUi, HabitsView.Calendar>
    ) : HabitListUiMapper {

        override fun mapCalendar(habits: List<HabitWithEntries>, columnsNumber: Int) =
            habits.map { it.map(mapperCalendar, columnsNumber) }

        override fun mapDaily(habits: List<HabitWithEntries>, entriesNumber: Int) =
            habits.map { habit ->
                val entries = habit.entries.entries.filter { it.key < entriesNumber }
                habit.copy(entries = EntryList(entries)).map(mapperDaily, entriesNumber)
            }
    }
}