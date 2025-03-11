package com.skytoph.taski.presentation.core.interactor

import androidx.compose.material3.SnackbarDuration
import com.skytoph.taski.R
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.appbar.PopupMessage
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource

interface ArchiveHabitInteractor {
    suspend fun archive(id: Long, archived: String, archive: Boolean = true)

    class Base(
        private val repository: HabitRepository,
        private val popup: PopupMessage.Show<SnackbarMessage>,
    ) : ArchiveHabitInteractor {

        override suspend fun archive(id: Long, archived: String, archive: Boolean) {
            val habit = repository.habit(id)
            repository.update(habit.copy(isArchived = archive))
            val icon = IconResource.Id(if (archive) R.drawable.archive_down else R.drawable.archive_up)
            val message = SnackbarMessage(
                messageResource = StringResource.Value(archived),
                title = StringResource.Value(habit.title),
                icon = icon,
                color = habit.color,
                duration = SnackbarDuration.Short
            )
            popup.show(message)
        }
    }
}