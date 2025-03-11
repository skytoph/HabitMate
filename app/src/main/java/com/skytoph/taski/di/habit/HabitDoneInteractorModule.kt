package com.skytoph.taski.di.habit

import com.skytoph.taski.core.Now
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HabitDoneInteractorModule {

    @Provides
    fun interactor(now: Now, repository: HabitRepository): HabitDoneInteractor =
        HabitDoneInteractor.Base(repository, now)
}