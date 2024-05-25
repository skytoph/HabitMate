package com.github.skytoph.taski.presentation.habit.edit

import android.content.Context
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper

interface EditHabitInteractor : HabitDoneInteractor {
    suspend fun habit(id: Long): Habit
    suspend fun insert(habit: HabitUi, context: Context)

    class Base(
        private val mapper: HabitDomainMapper,
        private val repository: HabitRepository,
        now: Now,
    ) : EditHabitInteractor, HabitDoneInteractor by HabitDoneInteractor.Base(repository, now) {

        override suspend fun habit(id: Long) = repository.habit(id)

        override suspend fun insert(habit: HabitUi, context: Context) =
            repository.update(habit.map(mapper, context))
    }
}