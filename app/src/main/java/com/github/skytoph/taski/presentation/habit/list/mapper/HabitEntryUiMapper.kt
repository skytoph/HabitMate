package com.github.skytoph.taski.presentation.habit.list.mapper

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi

interface HabitHistoryUiMapper<T : HabitHistoryUi> {
    fun map(
        page: Int = 0, goal: Int = 0, history: EntryList, habitColor: Color, defaultColor: Color
    ): T
}