package com.skytoph.taski.presentation.settings.archive

import com.skytoph.taski.core.datastore.settings.FilterHabits
import com.skytoph.taski.core.reminder.ReminderScheduler
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.appbar.PopupMessage
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.core.interactor.ArchiveHabitInteractor
import com.skytoph.taski.presentation.core.interactor.DeleteHabitInteractor
import com.skytoph.taski.presentation.core.interactor.GetHabitsInteractor

interface UnarchiveHabitsInteractor :
    ArchiveHabitInteractor,
    DeleteHabitInteractor,
    GetHabitsInteractor.HabitsFlow {

    class Base(
        private val repository: HabitRepository,
        private val popup: PopupMessage.Show<SnackbarMessage>,
        scheduler: ReminderScheduler,
    ) : UnarchiveHabitsInteractor,
        ArchiveHabitInteractor by ArchiveHabitInteractor.Base(repository, popup),
        DeleteHabitInteractor by DeleteHabitInteractor.Base(repository, popup, scheduler),
        GetHabitsInteractor.HabitsFlow by GetHabitsInteractor.Base(
            repository = repository,
            filter = FilterHabits.Archived(true)
        )
}