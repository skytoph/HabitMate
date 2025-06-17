package com.skytoph.taski.presentation.habit.details

import com.skytoph.taski.presentation.habit.edit.EntryEditableUi

data class EntryUpdated(
    val entry: EntryEditableUi,
    val barChartEntry: BarItemUi,
    val barChartLastItemTimestamp: Long? = 0,
)