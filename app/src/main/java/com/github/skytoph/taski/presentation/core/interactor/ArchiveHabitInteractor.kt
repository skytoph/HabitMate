package com.github.skytoph.taski.presentation.core.interactor

import com.github.skytoph.taski.R
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.appbar.PopupMessage
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.state.IconResource

interface ArchiveHabitInteractor {
    suspend fun archive(id: Long, archived: String, archive: Boolean = true)

    class Base(
        private val repository: HabitRepository,
        private val popup: PopupMessage.Show<SnackbarMessage>,
    ) : ArchiveHabitInteractor {

        override suspend fun archive(id: Long, archived: String, archive: Boolean) {
            val habit = repository.habit(id)
            repository.update(habit.copy(isArchived = archive))
            val icon =
                IconResource.Id(if (archive) R.drawable.archive_down else R.drawable.archive_up)
            popup.show(SnackbarMessage(archived, habit.title, icon))
        }
    }
}