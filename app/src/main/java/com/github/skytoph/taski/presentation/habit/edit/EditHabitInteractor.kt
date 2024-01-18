package com.github.skytoph.taski.presentation.habit.edit

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.list.HabitDoneInteractor
import kotlinx.coroutines.flow.Flow

interface EditHabitInteractor : HabitDoneInteractor {
    fun habit(id: Long): Flow<Habit>
    suspend fun insert(habit: Habit)
    suspend fun delete(id: Long)

    class Base(repository: HabitRepository, now: Now) :
        EditHabitInteractor, HabitDoneInteractor.Abstract(repository, now) {

        override fun habit(id: Long) = repository.habit(id)

        override suspend fun insert(habit: Habit) = repository.insert(habit)

        override suspend fun delete(id: Long) = repository.delete(id)
    }
}