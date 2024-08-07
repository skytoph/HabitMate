package com.github.skytoph.taski.presentation.settings.archive

import com.github.skytoph.taski.core.reminder.ReminderScheduler
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.appbar.PopupMessage
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.interactor.ArchiveHabitInteractor
import com.github.skytoph.taski.presentation.core.interactor.DeleteHabitInteractor
import com.github.skytoph.taski.presentation.core.interactor.GetHabitsInteractor
import com.github.skytoph.taski.presentation.habit.list.view.FilterHabits

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