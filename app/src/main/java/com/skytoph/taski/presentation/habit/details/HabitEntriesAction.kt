package com.skytoph.taski.presentation.habit.details

import com.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import kotlinx.coroutines.flow.MutableStateFlow

abstract class HabitEntriesAction {
    abstract fun apply(data: EditableHistoryUi): EditableHistoryUi
    open fun apply(data: BarItemUi): BarItemUi = data
    fun handle(state: MutableStateFlow<List<HabitEntriesAction>>) {
        state.value += this
    }

    class UpdateEntry(private val entry: EntryUpdated) : HabitEntriesAction() {
        override fun apply(data: EditableHistoryUi): EditableHistoryUi =
            if (!data.entries.containsKey(entry.entry.daysAgo)) data
            else data.copy(entries = data.entries.toMutableMap().apply {
                this[entry.entry.daysAgo] = entry.entry.copy(isDisabled = this[entry.entry.daysAgo]?.isDisabled == true)
            })

        override fun apply(data: BarItemUi): BarItemUi =
            when {
                data.timestamp == entry.barChartEntry.timestamp -> entry.barChartEntry

                entry.barChartLastItemTimestamp == null
                        || data.timestamp < entry.barChartLastItemTimestamp -> data.copy(isEmpty = true)

                data.timestamp > entry.barChartLastItemTimestamp -> data.copy(isEmpty = false)

                else -> data
            }
    }

    class ApplyStatistics(private val statistics: HabitStatisticsUi) :
        HabitEntriesAction() {
        override fun apply(data: EditableHistoryUi): EditableHistoryUi =
            data.copy(entries = data.entries.mapValues { (daysAgo, entry) ->
                entry.copy(streakType = statistics.type(daysAgo))
            })
    }
}