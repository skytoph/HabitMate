package com.github.skytoph.taski.presentation.core.interactor

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.HabitUi

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