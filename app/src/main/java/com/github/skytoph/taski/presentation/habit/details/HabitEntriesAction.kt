package com.github.skytoph.taski.presentation.habit.details

import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import kotlinx.coroutines.flow.MutableStateFlow

abstract class HabitEntriesAction {
    abstract fun apply(data: EditableHistoryUi): EditableHistoryUi
    fun handle(state: MutableStateFlow<List<HabitEntriesAction>>) {
        state.value += this
    }

    class UpdateEntry(private val entry: EntryEditableUi) : HabitEntriesAction() {
        override fun apply(data: EditableHistoryUi): EditableHistoryUi =
            if (!data.entries.containsKey(entry.daysAgo)) data
            else data.copy(entries = data.entries.toMutableMap().apply { this[entry.daysAgo] = entry })
    }

    class ApplyStatistics(private val statistics: HabitStatisticsUi, private val isBorderOn: Boolean) : HabitEntriesAction() {
        override fun apply(data: EditableHistoryUi): EditableHistoryUi =
            data.copy(entries = data.entries.entries.associate { entry ->
                entry.key to entry.value.copy(hasBorder = isBorderOn && entry.value.timesDone == 0 && statistics.isInRange(entry.key))
            })
    }
}