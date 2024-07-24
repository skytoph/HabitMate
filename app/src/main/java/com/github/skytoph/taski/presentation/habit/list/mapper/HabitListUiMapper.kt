package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.view.ViewType

interface HabitListUiMapper {
    fun mapCalendar(
        habits: List<HabitWithEntries>, columnsNumber: Int, isBorderOn: Boolean, isFirstDaySunday: Boolean
    ): List<HabitWithHistoryUi<HistoryUi>>

    fun mapDaily(habits: List<HabitWithEntries>, entriesNumber: Int, isBorderOn: Boolean)
            : List<HabitWithHistoryUi<HistoryUi>>

    class Base(
        private val mapperDaily: HabitWithHistoryUiMapper<HistoryUi, ViewType.Daily>,
        private val mapperCalendar: HabitWithHistoryUiMapper<HistoryUi, ViewType.Calendar>
    ) : HabitListUiMapper {

        override fun mapCalendar(
            habits: List<HabitWithEntries>, columnsNumber: Int, isBorderOn: Boolean, isFirstDaySunday: Boolean
        ) = habits.map { it.map(mapperCalendar, columnsNumber, isBorderOn, isFirstDaySunday) }

        override fun mapDaily(
            habits: List<HabitWithEntries>, entriesNumber: Int, isBorderOn: Boolean
        ) = habits.map { habit ->
            val entries = habit.entries.entries.filter { it.key < entriesNumber }
            habit.copy(entries = EntryList(entries)).map(mapperDaily, entriesNumber, isBorderOn, false)
        }
    }
}