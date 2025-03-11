package com.skytoph.taski.presentation.core.interactor

import com.skytoph.taski.core.Now
import com.skytoph.taski.domain.habit.Entry
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.presentation.habit.HabitUi

interface HabitDoneInteractor {
    suspend fun habitDone(habit: HabitUi, daysAgo: Int = 0): Entry
    suspend fun entry(id: Long, daysAgo: Int): Entry

    class Base(private val repository: HabitRepository, private val now: Now) :
        HabitDoneInteractor {

        override suspend fun entry(id: Long, daysAgo: Int): Entry {
            val timestamp = now.daysAgoInMillis(daysAgo)
            return repository.entry(id, timestamp) ?: Entry(timestamp, 0)
        }

        override suspend fun habitDone(habit: HabitUi, daysAgo: Int): Entry {
            val entry = entry(habit.id, daysAgo)
            val timesDone = entry.timesDone.plus(1)
            val newEntry = entry.copy(timesDone = if (timesDone <= habit.goal) timesDone else 0)
            if (timesDone <= habit.goal)
                repository.insertEntry(habit.id, newEntry)
            else
                repository.deleteEntry(habit.id, newEntry)
            return newEntry
        }
    }
}