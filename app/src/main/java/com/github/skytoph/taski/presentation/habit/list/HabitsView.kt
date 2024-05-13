package com.github.skytoph.taski.presentation.habit.list

import androidx.compose.runtime.Stable
import com.github.skytoph.taski.R
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.core.state.StringResource
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitListUiMapper

sealed class HabitsView(open val entries: Int) {
    abstract fun map(mapper: HabitListUiMapper, habits: List<HabitWithEntries>)
            : List<HabitWithHistoryUi<HistoryUi>>

    abstract fun withEntries(entries: Int): HabitsView

    data class Calendar(private val columns: Int = 0) : HabitsView(columns) {
        override fun map(mapper: HabitListUiMapper, habits: List<HabitWithEntries>) =
            mapper.mapCalendar(habits, columns)

        override fun withEntries(entries: Int): HabitsView = Calendar(entries)
    }

    data class Daily(override val entries: Int = 0) : HabitsView(entries) {
        override fun map(mapper: HabitListUiMapper, habits: List<HabitWithEntries>) =
            mapper.mapDaily(habits, entries)

        override fun withEntries(numberOfEntries: Int): HabitsView = Daily(numberOfEntries)
    }
}

@Stable
data class HabitsViewOption(val title: StringResource, val view: HabitsView) {
    companion object {
        val list = listOf(
            HabitsViewOption(StringResource.ResId(R.string.option_calendar), HabitsView.Calendar()),
            HabitsViewOption(StringResource.ResId(R.string.option_daily), HabitsView.Daily())
        )
    }
}