package com.skytoph.taski.presentation.core.interactor

import com.skytoph.taski.core.datastore.settings.FilterHabits
import com.skytoph.taski.core.datastore.settings.SortHabits
import com.skytoph.taski.domain.habit.Habit
import com.skytoph.taski.domain.habit.HabitRepository
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

        override fun habitsFlow(): Flow<List<Habit>> = repository.habitsFlow().map { it.filterAndSort() }

        override suspend fun habits(): List<Habit> = repository.habits().filterAndSort()

        private fun List<Habit>.filterAndSort(): List<Habit> {
            val filtered = filter?.filter(this) ?: this
            return sort?.sort(filtered) ?: filtered
        }
    }
}