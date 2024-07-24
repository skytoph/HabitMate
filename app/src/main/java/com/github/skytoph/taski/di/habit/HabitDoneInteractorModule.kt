package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
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