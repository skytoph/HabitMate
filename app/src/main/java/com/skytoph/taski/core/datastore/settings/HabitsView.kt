package com.skytoph.taski.core.datastore.settings

import com.skytoph.taski.domain.habit.HabitWithEntries
import com.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.skytoph.taski.presentation.habit.list.HistoryUi
import com.skytoph.taski.presentation.habit.list.mapper.HabitsViewMapper

data class HabitsView(
    val viewType: ViewType = ViewType.Daily(),
    val sortBy: SortHabits = SortHabits.Manually,
    val filterBy: FilterHabits = FilterHabits.None,
    val showTodayHabitsOnly: Boolean = false,
    val initialized: Boolean = false
) {
    fun map(mapper: HabitsViewMapper, habits: List<HabitWithEntries>, settings: Settings)
            : List<HabitWithHistoryUi<HistoryUi>>? =
        if (!initialized) null
        else mapper.map(habits, this, settings.currentDayHighlighted, settings.weekStartsOnSunday.value)
}

sealed class HabitHistoryView {
    data object Heatmap : HabitHistoryView()
    data object Calendar : HabitHistoryView()
}

data class HabitDetailsView(
    val barChartFrequency: ChartFrequency = ChartFrequency.Default
)

sealed interface ChartFrequency {
    data object Year : ChartFrequency
    data object Month : ChartFrequency
    data object Week : ChartFrequency
    data object Day : ChartFrequency

    companion object {
        val Default: ChartFrequency = Week
    }
}