package com.skytoph.taski.presentation.habit.details

import com.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.skytoph.taski.presentation.habit.edit.EntryEditableUi
import kotlinx.coroutines.flow.MutableStateFlow

abstract class HabitEntriesAction {
    abstract fun apply(data: EditableHistoryUi): EditableHistoryUi
    fun handle(state: MutableStateFlow<List<HabitEntriesAction>>) {
        state.value += this
    }

    class UpdateEntry(private val entry: EntryEditableUi) : HabitEntriesAction() {
        override fun apply(data: EditableHistoryUi): EditableHistoryUi =
            if (!data.entries.containsKey(entry.daysAgo)) data
            else data.copy(entries = data.entries.toMutableMap().apply {
                this[entry.daysAgo] = entry.copy(isDisabled = this[entry.daysAgo]?.isDisabled ?: false)
            })
    }

    class ApplyStatistics(private val statistics: HabitStatisticsUi, private val isBorderOn: Boolean) :
        HabitEntriesAction() {
        override fun apply(data: EditableHistoryUi): EditableHistoryUi =
            data.copy(entries = data.entries.entries.associate { entry ->
                entry.key to entry.value.copy(streakType = if (isBorderOn) statistics.type(entry.key) else null)
            })
    }
}