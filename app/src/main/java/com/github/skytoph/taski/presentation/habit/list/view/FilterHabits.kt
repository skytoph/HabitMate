package com.github.skytoph.taski.presentation.habit.list.view

import com.github.skytoph.taski.core.Matches
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitWithEntries

interface FilterHabits : Matches<FilterHabits> {
    fun predicate(todayDone: Int = 0): (Habit) -> Boolean

    fun <T> filter(habits: List<T>, selector: (T) -> Habit, today: (T) -> Int = { 0 }): List<T> =
        habits.filter { data -> predicate(today(data)).invoke(selector(data)) }

    fun filter(habits: List<Habit>): List<Habit> =
        habits.filter(predicate())

    override fun matches(item: FilterHabits): Boolean = this == item

    object None : FilterHabits {
        override fun predicate(todayDone: Int): (Habit) -> Boolean = { true }
        override fun filter(habits: List<Habit>): List<Habit> = habits
        override fun <T> filter(
            habits: List<T>, selector: (T) -> Habit, today: (T) -> Int
        ): List<T> = habits
    }

    object ByState : FilterHabits {
        override fun predicate(todayDone: Int): (Habit) -> Boolean = { todayDone < it.goal }
    }

    object NotArchived : FilterHabits {
        override fun predicate(todayDone: Int): (Habit) -> Boolean = { !it.isArchived }
    }
}