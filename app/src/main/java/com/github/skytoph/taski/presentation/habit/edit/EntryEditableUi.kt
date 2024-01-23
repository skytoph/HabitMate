package com.github.skytoph.taski.presentation.habit.edit

import com.github.skytoph.taski.presentation.habit.HabitEntryUi

data class EntryEditableUi(val day: String, val colorPercent: Float = 0F, val daysAgo: Int) :
    HabitEntryUi