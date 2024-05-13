package com.github.skytoph.taski.presentation.habit.list

import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi

data class HabitListState(
    val habits: List<HabitWithHistoryUi<HistoryUi>> = emptyList(),
    val isLoading: Boolean = true,
    val isViewTypeVisible: Boolean = false
)