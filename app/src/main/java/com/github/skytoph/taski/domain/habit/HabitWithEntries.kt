package com.github.skytoph.taski.domain.habit

import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitWithHistoryUiMapper
import com.github.skytoph.taski.presentation.habit.list.view.ViewType

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