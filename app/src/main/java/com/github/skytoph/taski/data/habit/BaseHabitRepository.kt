package com.github.skytoph.taski.data.habit

import com.github.skytoph.taski.data.habit.database.HabitDao
import com.github.skytoph.taski.data.habit.mapper.toHabit
import com.github.skytoph.taski.data.habit.mapper.toHabitDB
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BaseHabitRepository(private val habitDao: HabitDao) : HabitRepository {

    override suspend fun habits(): Flow<List<Habit>> =
        habitDao.habits().map { list -> list.map { it.toHabit() } }

    override suspend fun habitWithDetails(id: Long): Habit = habitDao.habit(id).toHabit()

    override suspend fun insert(habit: Habit) = habitDao.insert(habit.toHabitDB())

    override suspend fun delete(id: Long) = habitDao.delete(id)
}