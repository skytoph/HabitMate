package com.github.skytoph.taski.domain.habit

import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    suspend fun habits(): Flow<List<Habit>>
    suspend fun habitWithDetails(id: Long): Habit
    suspend fun insert(habit: Habit)
    suspend fun delete(id: Long)
}