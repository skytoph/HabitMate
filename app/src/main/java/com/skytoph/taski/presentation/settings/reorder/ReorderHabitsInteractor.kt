package com.skytoph.taski.presentation.settings.reorder

import android.content.Context
import com.skytoph.taski.core.datastore.settings.FilterHabits
import com.skytoph.taski.core.datastore.settings.SortHabits
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.core.interactor.GetHabitsInteractor
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper

interface ReorderHabitsInteractor : GetHabitsInteractor.HabitsList {
    suspend fun update(habits: List<HabitUi>, context: Context)

    class Base(
        private val repository: HabitRepository,
        private val mapper: HabitDomainMapper
    ) : ReorderHabitsInteractor,
        GetHabitsInteractor.HabitsList by GetHabitsInteractor.Base(
            repository = repository, filter = FilterHabits.Archived(), sort = SortHabits.Manually
        ) {

        override suspend fun update(habits: List<HabitUi>, context: Context) =
            habits.forEachIndexed { index, habit ->
                repository.update(habit.copy(priority = index).map(mapper, context))
            }
    }
}
