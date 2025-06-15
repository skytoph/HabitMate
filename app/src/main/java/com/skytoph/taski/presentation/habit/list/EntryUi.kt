package com.skytoph.taski.presentation.habit.list

import com.skytoph.taski.presentation.habit.HabitEntryUi
import com.skytoph.taski.presentation.habit.HabitHistoryUi

data class HistoryUi(
    val entries: Map<Int, EntryUi> = emptyMap(),
    val todayDonePercent: Float = 0F,
    val todayDone: Int = 0,
    val fromDaysAgo: Int = 0,
    val toDaysAgo: Int = entries.size
) : HabitHistoryUi

data class EntryUi(
    val daysAgo: Int = 0,
    val percentDone: Float = 0F,
    val timesDone: Int = 0,
    val hasBorder: Boolean = false,
) : HabitEntryUi
