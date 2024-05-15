package com.github.skytoph.taski.presentation.habit.list.view

import com.github.skytoph.taski.core.Matches
import com.github.skytoph.taski.domain.habit.HabitWithEntries

interface SortHabits : Matches<SortHabits> {
    fun sort(habits: List<HabitWithEntries>): List<HabitWithEntries>
    override fun matches(item: SortHabits): Boolean = this == item

    object ByTitle : SortHabits {
        override fun sort(habits: List<HabitWithEntries>): List<HabitWithEntries> =
            habits.sortedBy { it.habit.title }
    }

    object ByColor : SortHabits {
        override fun sort(habits: List<HabitWithEntries>): List<HabitWithEntries> =
            habits.sortedBy { it.habit.color }
    }

    object Manually : SortHabits {
        override fun sort(habits: List<HabitWithEntries>): List<HabitWithEntries> =
            habits.sortedBy { it.habit.priority }
    }
}