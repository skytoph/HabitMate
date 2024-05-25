package com.github.skytoph.taski.presentation.settings.archive

import com.github.skytoph.taski.presentation.habit.HabitUi

data class ArchiveState(
    val habits: List<HabitUi> = emptyList(),
    val deleteHabitById: Long? = null,
    val restoreHabitById: Long? = null,
)