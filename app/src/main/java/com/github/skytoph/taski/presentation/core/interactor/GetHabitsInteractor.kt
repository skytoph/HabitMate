package com.github.skytoph.taski.presentation.core.interactor

import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.list.view.FilterHabits
import com.github.skytoph.taski.presentation.habit.list.view.SortHabits
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetHabitsInteractor {
    interface HabitsList {
        suspend fun habits(): List<Habit>
    }

    interface HabitsFlow {
        fun habitsFlow(): Flow<List<Habit>>
    }

    class Base(
        private val repository: HabitRepository,
        private val sort: SortHabits? = null,
        private val filter: FilterHabits? = null,
    ) : GetHabitsInteractor, HabitsList, HabitsFlow {

        override fun habitsFlow(): Flow<List<Habit>> = repository.habitsFlow()
            .map { it.filterAndSort() }

        override suspend fun habits(): List<Habit> = repository.habits().filterAndSort()

        private fun List<Habit>.filterAndSort(): List<Habit> {
            val filtered = filter?.filter(this) ?: this
            return sort?.sort(filtered) ?: filtered
        }
    }
}