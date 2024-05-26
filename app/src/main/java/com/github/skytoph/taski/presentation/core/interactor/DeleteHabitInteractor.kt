package com.github.skytoph.taski.presentation.core.interactor

import androidx.compose.material3.SnackbarDuration
import com.github.skytoph.taski.R
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.appbar.PopupMessage
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.state.IconResource

interface DeleteHabitInteractor {
    suspend fun delete(id: Long, message: String)

    class Base(
        private val repository: HabitRepository,
        private val popup: PopupMessage.Show<SnackbarMessage>,
    ) : DeleteHabitInteractor {
        override suspend fun delete(id: Long, message: String) {
            val habit = repository.habit(id).title
            repository.delete(id)
            val message = SnackbarMessage(
                message = message,
                title = habit,
                icon = IconResource.Id(R.drawable.trash),
                duration = SnackbarDuration.Short
            )
            popup.show(message)
        }
    }
}