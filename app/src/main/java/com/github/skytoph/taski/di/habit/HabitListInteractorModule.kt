package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.alarm.ReminderScheduler
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.appbar.PopupMessage
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import com.github.skytoph.taski.presentation.habit.list.HabitListInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HabitListInteractorModule {

    @Provides
    fun interactor(
        repository: HabitRepository,
        popup: PopupMessage.Show<SnackbarMessage>,
        scheduler: ReminderScheduler,
        interactor: HabitDoneInteractor
    ): HabitListInteractor = HabitListInteractor.Base(repository, popup, scheduler, interactor)
}