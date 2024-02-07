package com.github.skytoph.taski.presentation.habit.edit

import androidx.paging.PagingData
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.EntityPagerProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.HabitDoneInteractor
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import kotlinx.coroutines.flow.Flow

interface EditHabitInteractor : HabitDoneInteractor {
    fun entries(id: Long): Flow<PagingData<EditableHistoryUi>>
    suspend fun habit(id: Long): Habit
    suspend fun insert(habit: HabitUi)
    suspend fun delete(id: Long)

    class Base(
        private val mapper: HabitDomainMapper,
        private val pagerProvider: EntityPagerProvider,
        repository: HabitRepository,
        now: Now,
    ) : EditHabitInteractor, HabitDoneInteractor.Abstract(repository, now) {

        override fun entries(id: Long): Flow<PagingData<EditableHistoryUi>> =
            pagerProvider.getEntries(id)

        override suspend fun habit(id: Long) = repository.habit(id)

        override suspend fun insert(habit: HabitUi) = repository.update(habit.map(mapper))

        override suspend fun delete(id: Long) = repository.delete(id)
    }
}