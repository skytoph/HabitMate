package com.github.skytoph.taski.presentation.habit.list

import com.github.skytoph.taski.presentation.habit.HabitEntryUi
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi

data class HistoryUi(
    val entries: List<EntryUi> = emptyList(),
    val todayPosition: Int = 0
) : HabitHistoryUi

data class EntryUi(
    val colorPercent: Float = 0F,
    val hasBorder: Boolean = false
) : HabitEntryUi
