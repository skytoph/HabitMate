package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi
import com.github.skytoph.taski.presentation.habit.details.HabitStatisticsUi
import com.github.skytoph.taski.presentation.habit.list.view.ViewType

interface HabitHistoryUiMapper<T : HabitHistoryUi, V : ViewType> {
    fun map(page: Int = 0, goal: Int = 0, history: EntryList, stats: HabitStatisticsUi): T
}