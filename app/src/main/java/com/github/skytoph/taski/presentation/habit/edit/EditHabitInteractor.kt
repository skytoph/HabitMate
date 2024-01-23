package com.github.skytoph.taski.presentation.habit.edit

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.HabitDoneInteractor
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import kotlinx.coroutines.flow.Flow

interface EditHabitInteractor : HabitDoneInteractor {
    fun habit(id: Long): Flow<Habit>
    suspend fun insert(habit: HabitUi<*>)
    suspend fun delete(id: Long)

    class Base(repository: HabitRepository, now: Now, private val mapper: HabitDomainMapper) :
        EditHabitInteractor, HabitDoneInteractor.Abstract(repository, now) {

        override fun habit(id: Long) = repository.habit(id)

        override suspend fun insert(habit: HabitUi<*>) = repository.update(habit.map(mapper))

        override suspend fun delete(id: Long) = repository.delete(id)
    }
}