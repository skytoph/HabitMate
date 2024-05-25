package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import com.github.skytoph.taski.presentation.settings.reorder.ReorderHabitsInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ReorderHabitsInteractorModule {

    @Provides
    fun interactor(
        repository: HabitRepository, mapper: HabitDomainMapper
    ): ReorderHabitsInteractor = ReorderHabitsInteractor.Base(repository, mapper)
}