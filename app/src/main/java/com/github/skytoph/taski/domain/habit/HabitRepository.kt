package com.github.skytoph.taski.domain.habit

import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun habits(): Flow<List<HabitWithEntries>>
    fun entries(id: Long): Flow<EntryList>
    suspend fun habit(id: Long): Habit
    suspend fun entry(id: Long, timestamp: Long): Entry?
    suspend fun insert(habit: Habit)
    suspend fun update(habit: Habit)
    suspend fun insertEntry(id: Long, entry: Entry)
    suspend fun deleteEntry(id: Long, entry: Entry)
    suspend fun delete(id: Long)
}