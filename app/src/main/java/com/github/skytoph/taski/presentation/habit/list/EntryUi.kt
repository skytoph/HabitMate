package com.github.skytoph.taski.presentation.habit.list

import com.github.skytoph.taski.presentation.habit.HabitEntryUi
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi

data class HistoryUi(
    val entries: List<EntryUi> = emptyList(),
    val todayDonePercent: Float = 0F,
    val todayDone: Int = 0,
) : HabitHistoryUi

data class EntryUi(
    val daysAgo: Int = 0,
    val percentDone: Float = 0F,
    val hasBorder: Boolean = false,
) : HabitEntryUi
