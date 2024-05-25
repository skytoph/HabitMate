package com.github.skytoph.taski.presentation.settings.reorder

import android.content.Context
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.core.interactor.GetHabitsInteractor
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import com.github.skytoph.taski.presentation.habit.list.view.FilterHabits
import com.github.skytoph.taski.presentation.habit.list.view.SortHabits

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
