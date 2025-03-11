package com.skytoph.taski.presentation.habit.list

import com.skytoph.taski.core.reminder.ReminderScheduler
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.domain.habit.HabitWithEntries
import com.skytoph.taski.presentation.appbar.PopupMessage
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.core.interactor.ArchiveHabitInteractor
import com.skytoph.taski.presentation.core.interactor.DeleteHabitInteractor
import com.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
import kotlinx.coroutines.flow.Flow

interface HabitListInteractor : HabitDoneInteractor, DeleteHabitInteractor, ArchiveHabitInteractor {
    fun habits(): Flow<List<HabitWithEntries>>

    class Base(
        private val repository: HabitRepository,
        private val popup: PopupMessage.Show<SnackbarMessage>,
        scheduler: ReminderScheduler,
        habitInteractor: HabitDoneInteractor,
    ) : HabitListInteractor,
        DeleteHabitInteractor by DeleteHabitInteractor.Base(repository, popup, scheduler),
        ArchiveHabitInteractor by ArchiveHabitInteractor.Base(repository, popup),
        HabitDoneInteractor by habitInteractor {

        override fun habits(): Flow<List<HabitWithEntries>> = repository.habitsWithEntries()
    }
}