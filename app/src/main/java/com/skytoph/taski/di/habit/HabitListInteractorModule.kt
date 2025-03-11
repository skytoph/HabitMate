package com.skytoph.taski.di.habit

import com.skytoph.taski.core.reminder.ReminderScheduler
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.appbar.PopupMessage
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import com.skytoph.taski.presentation.habit.list.HabitListInteractor
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