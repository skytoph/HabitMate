package com.github.skytoph.taski.presentation.habit.list

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.HabitUi
import kotlinx.coroutines.flow.Flow

interface HabitListInteractor : HabitDoneInteractor {
    fun habits(): Flow<List<HabitWithEntries>>

    class Base(repository: HabitRepository, now: Now) :
        HabitListInteractor, HabitDoneInteractor.Abstract(repository, now) {

        override fun habits(): Flow<List<HabitWithEntries>> = repository.habits()
    }
}

interface HabitDoneInteractor {
    suspend fun habitDone(habit: HabitUi, daysAgo: Int = 0)

    abstract class Abstract(protected val repository: HabitRepository, protected val now: Now) :
        HabitDoneInteractor {

        override suspend fun habitDone(habit: HabitUi, daysAgo: Int) {
            val timestamp = now.daysAgoInMillis(daysAgo)
            val entry = repository.entry(habit.id, timestamp)
            val timesDone = entry?.timesDone?.plus(1) ?: 1
            val newEntry = entry?.copy(timesDone = timesDone) ?: Entry(timestamp, timesDone)
            if (timesDone <= habit.goal)
                repository.insertEntry(habit.id, newEntry)
            else
                repository.deleteEntry(habit.id, newEntry)
        }
    }
}