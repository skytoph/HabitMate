package com.github.skytoph.taski.presentation.habit.list

import com.github.skytoph.taski.core.alarm.ReminderScheduler
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.appbar.PopupMessage
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.interactor.ArchiveHabitInteractor
import com.github.skytoph.taski.presentation.core.interactor.DeleteHabitInteractor
import com.github.skytoph.taski.presentation.core.interactor.HabitDoneInteractor
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