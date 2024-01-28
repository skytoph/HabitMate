package com.github.skytoph.taski.presentation.habit.list

import com.github.skytoph.taski.presentation.habit.HabitUi

data class HabitListState(
    val habits: List<HabitUi<HistoryUi>> = emptyList(),
    val isLoading: Boolean = true
)