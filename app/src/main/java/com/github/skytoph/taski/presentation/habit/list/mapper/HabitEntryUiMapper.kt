package com.github.skytoph.taski.presentation.habit.list.mapper

import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.presentation.habit.HabitEntryUi

interface HabitEntryUiMapper<T : HabitEntryUi> {
    fun map(goal: Int, history: Map<Int, Entry>): List<T>
    fun todayPosition(): Int
}