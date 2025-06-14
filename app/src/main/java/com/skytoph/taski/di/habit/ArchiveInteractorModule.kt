package com.skytoph.taski.di.habit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.skytoph.taski.core.reminder.ReminderScheduler
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.appbar.PopupMessage
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.settings.archive.ArchiveState
import com.skytoph.taski.presentation.settings.archive.UnarchiveHabitsInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ArchiveInteractorModule {

    @Provides
    fun interactor(
        popup: PopupMessage.Show<SnackbarMessage>,
        repository: HabitRepository,
        scheduler: ReminderScheduler
    ): UnarchiveHabitsInteractor = UnarchiveHabitsInteractor.Base(repository, popup, scheduler)

    @Provides
    fun state(): MutableState<ArchiveState> = mutableStateOf(ArchiveState())
}