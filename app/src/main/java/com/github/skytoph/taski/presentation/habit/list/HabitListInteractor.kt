package com.github.skytoph.taski.presentation.habit.list

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.HabitUi
import kotlinx.coroutines.flow.Flow

interface HabitListInteractor {
    fun habits(): Flow<List<Habit>>
    suspend fun habitDone(habit: HabitUi, dayPosition: Int = habit.todayPosition)

    class Base(private val repository: HabitRepository, private val now: Now) :
        HabitListInteractor {

        override fun habits(): Flow<List<Habit>> = repository.habits()

        override suspend fun habitDone(habit: HabitUi, dayPosition: Int) {
            val timesDone = habit.history[dayPosition] ?: 0
            val entry = Entry(timestamp = now.dayInMillis(), timesDone = timesDone + 1)
            if (timesDone < habit.goal)
                repository.insertEntry(habit.id, entry)
            else
                repository.deleteEntry(habit.id, entry)
        }
    }
}
