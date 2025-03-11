package com.skytoph.taski.core.datastore.settings

import com.skytoph.taski.domain.habit.HabitWithEntries
import com.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.skytoph.taski.presentation.habit.list.HistoryUi
import com.skytoph.taski.presentation.habit.list.mapper.HabitListUiMapper
import com.skytoph.taski.presentation.habit.list.view.HabitsViewTypesProvider
import com.skytoph.taski.presentation.habit.list.view.OptionUi
import com.skytoph.taski.presentation.habit.list.view.ProvideOptionUi

sealed class ViewType : ProvideOptionUi<ViewType> {
    abstract fun map(
        mapper: HabitListUiMapper, habits: List<HabitWithEntries>, isBorderOn: Boolean, isFirstDaySunday: Boolean
    ): List<HabitWithHistoryUi<HistoryUi>>

    abstract fun withEntries(entries: Int): ViewType

    abstract val entries: Int

    data class Calendar(override val entries: Int = 0) : ViewType() {
        override fun map(
            mapper: HabitListUiMapper, habits: List<HabitWithEntries>, isBorderOn: Boolean, isFirstDaySunday: Boolean
        ) = mapper.mapCalendar(habits, entries, isBorderOn, isFirstDaySunday)

        override fun withEntries(entries: Int): ViewType = Calendar(entries)

        override fun optionUi(): OptionUi = HabitsViewTypesProvider.optionCalendar

        override fun matches(item: ViewType): Boolean = item is Calendar
    }

    data class Daily(override val entries: Int = 0) : ViewType() {
        override fun map(
            mapper: HabitListUiMapper, habits: List<HabitWithEntries>, isBorderOn: Boolean, isFirstDaySunday: Boolean
        ) = mapper.mapDaily(habits, entries, isBorderOn)

        override fun withEntries(numberOfEntries: Int): ViewType = Daily(numberOfEntries)

        override fun optionUi(): OptionUi = HabitsViewTypesProvider.optionDaily

        override fun matches(item: ViewType): Boolean = item is Daily
    }

    companion object {
        val options = listOf(Calendar(), Daily())
    }
}