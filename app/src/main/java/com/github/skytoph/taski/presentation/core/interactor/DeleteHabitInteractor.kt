package com.github.skytoph.taski.presentation.core.interactor

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.alarm.ReminderScheduler
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.appbar.PopupMessage
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.state.IconResource

interface DeleteHabitInteractor {
    suspend fun delete(id: Long, message: String, context: Context)

    class Base(
        private val repository: HabitRepository,
        private val popup: PopupMessage.Show<SnackbarMessage>,
        private val scheduler: ReminderScheduler,
    ) : DeleteHabitInteractor {
        override suspend fun delete(id: Long, message: String, context: Context) {
            val habit = repository.habit(id)
            scheduler.cancel(context, id, habit.frequency.times)
            repository.delete(id)
            val message = SnackbarMessage(
                message = message,
                title = habit.title,
                icon = IconResource.Id(R.drawable.trash),
                duration = SnackbarDuration.Short
            )
            popup.show(message)
        }
    }
}