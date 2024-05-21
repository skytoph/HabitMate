package com.github.skytoph.taski.presentation.habit.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.appbar.PopupMessage
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.habit.HabitUi
import kotlinx.coroutines.flow.Flow

interface HabitListInteractor : HabitDoneInteractor, DeleteHabitInteractor {
    fun habits(): Flow<List<HabitWithEntries>>
    suspend fun archive(id: Long, archived: String)

    class Base(
        private val repository: HabitRepository,
        private val popup: PopupMessage.Show<SnackbarMessage>,
        now: Now
    ) : HabitListInteractor,
        DeleteHabitInteractor by DeleteHabitInteractor.Base(repository, popup),
        HabitDoneInteractor by HabitDoneInteractor.Base(repository, now) {

        override fun habits(): Flow<List<HabitWithEntries>> = repository.habitsWithEntries()

        override suspend fun archive(id: Long, archived: String) {
            val habit = repository.habit(id)
            repository.update(habit.copy(isArchived = true))
            popup.show(SnackbarMessage(archived, habit.title, Icons.Default.Archive))
        }
    }
}

interface HabitDoneInteractor {
    suspend fun habitDone(habit: HabitUi, daysAgo: Int = 0)
    suspend fun entry(id: Long, daysAgo: Int): Entry

    class Base(private val repository: HabitRepository, private val now: Now) :
        HabitDoneInteractor {

        override suspend fun entry(id: Long, daysAgo: Int): Entry {
            val timestamp = now.daysAgoInMillis(daysAgo)
            return repository.entry(id, timestamp) ?: Entry(timestamp, 0)
        }

        override suspend fun habitDone(habit: HabitUi, daysAgo: Int) {
            val entry = entry(habit.id, daysAgo)
            val timesDone = entry.timesDone.plus(1)
            val newEntry = entry.copy(timesDone = timesDone)
            if (timesDone <= habit.goal)
                repository.insertEntry(habit.id, newEntry)
            else
                repository.deleteEntry(habit.id, newEntry)
        }
    }
}