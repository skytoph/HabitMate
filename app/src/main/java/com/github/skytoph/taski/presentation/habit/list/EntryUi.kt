package com.github.skytoph.taski.presentation.habit.list

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.presentation.habit.HabitEntryUi
import com.github.skytoph.taski.presentation.habit.HabitHistoryUi

data class HistoryUi(
    val entries: List<EntryUi> = emptyList(),
    val todayDonePercent: Float = 0F,
) : HabitHistoryUi

data class EntryUi(
    val color: Color,
    val hasBorder: Boolean = false
) : HabitEntryUi
