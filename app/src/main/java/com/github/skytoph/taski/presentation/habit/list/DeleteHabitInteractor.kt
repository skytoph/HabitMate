package com.github.skytoph.taski.presentation.habit.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.appbar.PopupMessage
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage

interface DeleteHabitInteractor {
    suspend fun delete(id: Long, message: String)

    class Base(
        private val repository: HabitRepository,
        private val popup: PopupMessage.Show<SnackbarMessage>,
    ) : DeleteHabitInteractor {
        override suspend fun delete(id: Long, message: String) {
            val habit = repository.habit(id).title
            repository.delete(id)
            popup.show(SnackbarMessage(message, habit, Icons.Default.Delete))
        }
    }
}