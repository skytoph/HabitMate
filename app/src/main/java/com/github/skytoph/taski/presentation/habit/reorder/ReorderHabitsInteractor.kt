package com.github.skytoph.taski.presentation.habit.reorder

import android.content.Context
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import com.github.skytoph.taski.presentation.habit.list.view.FilterHabits
import com.github.skytoph.taski.presentation.habit.list.view.SortHabits

interface ReorderHabitsInteractor {
    suspend fun habits(): List<Habit>
    suspend fun update(habits: List<HabitUi>, context: Context)

    class Base(
        private val repository: HabitRepository,
        private val mapper: HabitDomainMapper
    ) : ReorderHabitsInteractor {

        override suspend fun habits(): List<Habit> {
            val habits = repository.habits()
            val filtered = FilterHabits.NotArchived.filter(habits)
            val sorted = SortHabits.Manually.sort(filtered)
            return sorted
        }

        override suspend fun update(habits: List<HabitUi>, context: Context) =
            habits.forEachIndexed { index, habit ->
                repository.update(habit.copy(priority = index).map(mapper, context))
            }
    }
}
