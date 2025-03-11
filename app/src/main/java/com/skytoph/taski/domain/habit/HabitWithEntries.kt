package com.skytoph.taski.domain.habit

import com.skytoph.taski.core.datastore.settings.ViewType
import com.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.skytoph.taski.presentation.habit.list.HistoryUi
import com.skytoph.taski.presentation.habit.list.mapper.HabitWithHistoryUiMapper

data class HabitWithEntries(
    val habit: Habit,
    val entries: EntryList
) {

    fun <V : ViewType> map(
        mapper: HabitWithHistoryUiMapper<HistoryUi, V>,
        numberOfEntries: Int = 0,
        isBorderOn: Boolean,
        isFirstDaySunday: Boolean,
    ): HabitWithHistoryUi<HistoryUi> = mapper.map(this, numberOfEntries, isBorderOn, isFirstDaySunday)
}