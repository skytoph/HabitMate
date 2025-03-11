package com.skytoph.taski.presentation.habit.list.mapper

import com.skytoph.taski.core.datastore.settings.ViewType
import com.skytoph.taski.domain.habit.EntryList
import com.skytoph.taski.presentation.habit.HabitHistoryUi
import com.skytoph.taski.presentation.habit.details.HabitStatisticsUi

interface HabitHistoryUiMapper<T : HabitHistoryUi, V : ViewType> {
    fun map(
        page: Int = 0,
        goal: Int = 0,
        history: EntryList,
        stats: HabitStatisticsUi,
        isBorderOn: Boolean,
        isFirstDaySunday: Boolean,
    ): T
}