package com.github.skytoph.taski.presentation.habit.list.view

import com.github.skytoph.taski.core.Matches
import com.github.skytoph.taski.domain.habit.HabitWithEntries

interface FilterHabits : Matches<FilterHabits> {
    fun filter(habits: List<HabitWithEntries>): List<HabitWithEntries>
    override fun matches(item: FilterHabits): Boolean = this == item

    object None : FilterHabits {
        override fun filter(habits: List<HabitWithEntries>) = habits
    }

    object ByState : FilterHabits {
        override fun filter(habits: List<HabitWithEntries>) = habits.filter {
            val todayDoneTimes = it.entries.entries[0]?.timesDone ?: 0
            todayDoneTimes < it.habit.goal
        }
    }

    object NotArchived : FilterHabits {
        override fun filter(habits: List<HabitWithEntries>): List<HabitWithEntries> =
            habits.filter { !it.habit.isArchived }
    }
}