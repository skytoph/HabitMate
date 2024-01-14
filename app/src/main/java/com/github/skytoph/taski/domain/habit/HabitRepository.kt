package com.github.skytoph.taski.domain.habit

import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun habits(): Flow<List<Habit>>
    suspend fun habit(id: Long): Habit
    suspend fun insert(habit: Habit)
    suspend fun insertEntry(id: Long, entry: Entry)
    suspend fun deleteEntry(id: Long, entry: Entry)
    suspend fun delete(id: Long)
}