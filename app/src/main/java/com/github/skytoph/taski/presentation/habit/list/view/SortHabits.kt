package com.github.skytoph.taski.presentation.habit.list.view

import com.github.skytoph.taski.core.Matches
import com.github.skytoph.taski.domain.habit.Habit

sealed interface SortHabits : Matches<SortHabits> {
    fun comparator(): Comparator<Habit>

    fun sort(habits: List<Habit>): List<Habit> =
        habits.sortedWith(comparator())

    fun <T> sort(habits: List<T>, selector: (T) -> Habit): List<T> =
        habits.sortedWith(compareBy(comparator(), selector))

    override fun matches(item: SortHabits): Boolean = this == item

    data object ByTitle : SortHabits {
        override fun comparator(): Comparator<Habit> = compareBy { it.title }
    }

    data object ByColor : SortHabits {
        override fun comparator(): Comparator<Habit> = compareBy { it.color }
    }

    data object Manually : SortHabits {
        override fun comparator(): Comparator<Habit> = compareBy { it.priority }
    }
}