package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi

interface HabitHistoryUiMapper<T : HabitHistoryUi> {
    fun map(goal: Int, history: Map<Int, Entry>): T
}