package com.skytoph.taski.di.habit

import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.habit.create.CreateHabitInteractor
import com.skytoph.taski.presentation.habit.edit.NotificationInteractor
import com.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CreateHabitInteractorModule {

    @Provides
    fun interactor(
        repository: HabitRepository,
        mapper: HabitDomainMapper,
        interactor: NotificationInteractor
    ): CreateHabitInteractor = CreateHabitInteractor.Base(repository, mapper, interactor)
}