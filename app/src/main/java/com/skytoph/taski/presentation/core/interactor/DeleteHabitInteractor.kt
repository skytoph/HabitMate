package com.skytoph.taski.presentation.core.interactor

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import com.skytoph.taski.R
import com.skytoph.taski.core.reminder.ReminderScheduler
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.appbar.PopupMessage
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource

interface DeleteHabitInteractor {
    suspend fun delete(id: Long, message: String, context: Context)

    class Base(
        private val repository: HabitRepository,
        private val popup: PopupMessage.Show<SnackbarMessage>,
        private val scheduler: ReminderScheduler,
    ) : DeleteHabitInteractor {
        override suspend fun delete(id: Long, message: String, context: Context) {
            val habit = repository.habit(id)
            scheduler.cancel(id, habit.frequency.times)
            repository.delete(id)
            val message = SnackbarMessage(
                messageResource = StringResource.Value(message),
                title = StringResource.Value(habit.title),
                icon = IconResource.Id(R.drawable.trash),
                duration = SnackbarDuration.Short,
                isError = true
            )
            popup.show(message)
        }
    }
}