package com.github.skytoph.taski.presentation.habit.edit

import android.content.Context
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.HabitDoneInteractor
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper

interface EditHabitInteractor : HabitDoneInteractor {
    suspend fun habit(id: Long): Habit
    suspend fun insert(habit: HabitUi, context: Context)
    suspend fun delete(id: Long)

    class Base(
        private val mapper: HabitDomainMapper,
        repository: HabitRepository,
        now: Now,
    ) : EditHabitInteractor, HabitDoneInteractor.Abstract(repository, now) {

        override suspend fun habit(id: Long) = repository.habit(id)

        override suspend fun insert(habit: HabitUi, context: Context) =
            repository.update(habit.map(mapper, context))

        override suspend fun delete(id: Long) = repository.delete(id)
    }
}