package com.github.skytoph.taski.presentation.habit.list.view

import com.github.skytoph.taski.core.Matches
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitListUiMapper

sealed class ViewType(open val entries: Int): Matches<ViewType> {
    abstract fun map(mapper: HabitListUiMapper, habits: List<HabitWithEntries>)
            : List<HabitWithHistoryUi<HistoryUi>>

    abstract fun withEntries(entries: Int): ViewType

    data class Calendar(private val columns: Int = 0) : ViewType(columns) {
        override fun map(mapper: HabitListUiMapper, habits: List<HabitWithEntries>) =
            mapper.mapCalendar(habits, columns)

        override fun withEntries(entries: Int): ViewType = Calendar(entries)

        override fun matches(item: ViewType): Boolean = item is Calendar
    }

    data class Daily(override val entries: Int = 0) : ViewType(entries) {
        override fun map(mapper: HabitListUiMapper, habits: List<HabitWithEntries>) =
            mapper.mapDaily(habits, entries)

        override fun withEntries(numberOfEntries: Int): ViewType = Daily(numberOfEntries)

        override fun matches(item: ViewType): Boolean = item is Daily
    }
}