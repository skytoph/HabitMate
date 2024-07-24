package com.github.skytoph.taski.presentation.habit.list.view

import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper

sealed interface FilterHabits : ProvideOptionUi<FilterHabits> {
    fun predicate(todayDone: Int = 0): (Habit) -> Boolean

    fun <T> filter(habits: List<T>, selector: (T) -> Habit, today: (T) -> Int = { 0 }): List<T> =
        habits.filter { data -> predicate(today(data)).invoke(selector(data)) }

    fun filter(habits: List<Habit>): List<Habit> =
        habits.filter(predicate())

    override fun matches(item: FilterHabits): Boolean = this == item

    data object None : FilterHabits {
        override fun optionUi(): OptionUi = HabitsViewTypesProvider.optionFilterNone
        override fun predicate(todayDone: Int): (Habit) -> Boolean = { true }
        override fun filter(habits: List<Habit>): List<Habit> = habits
        override fun <T> filter(
            habits: List<T>, selector: (T) -> Habit, today: (T) -> Int
        ): List<T> = habits
    }

    data object ByState : FilterHabits {
        override fun optionUi(): OptionUi = HabitsViewTypesProvider.optionFilterByState
        override fun predicate(todayDone: Int): (Habit) -> Boolean = { todayDone < it.goal }
    }

    class Archived(private val archived: Boolean = false) : FilterHabits {
        override fun optionUi(): OptionUi = HabitsViewTypesProvider.optionFilterArchived
        override fun predicate(todayDone: Int): (Habit) -> Boolean = { it.isArchived == archived }
    }

    class Today(private val todayOnly: Boolean = false, private val mapper: StatisticsUiMapper) {
        fun filter(habits: List<HabitWithEntries>, isFirstDaySunday: Boolean): List<HabitWithEntries> =
            if (!todayOnly) habits else habits.filter { mapper.state(it, isFirstDaySunday).isScheduledForToday }
    }

    companion object {
        val options = listOf(None, ByState)
    }
}