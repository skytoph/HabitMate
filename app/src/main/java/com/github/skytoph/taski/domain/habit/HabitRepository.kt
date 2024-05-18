package com.github.skytoph.taski.domain.habit

import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun habitsWithEntries(): Flow<List<HabitWithEntries>>
    fun entriesFlow(id: Long): Flow<EntryList>
    fun habitFlow(id: Long): Flow<Habit?>
    suspend fun habits(): List<Habit>
    suspend fun habitWithEntries(id: Long): HabitWithEntries
    suspend fun entries(id: Long): EntryList
    suspend fun habit(id: Long): Habit
    suspend fun entry(id: Long, timestamp: Long): Entry?
    suspend fun insert(habit: Habit)
    suspend fun update(habit: Habit)
    suspend fun insertEntry(id: Long, entry: Entry)
    suspend fun deleteEntry(id: Long, entry: Entry)
    suspend fun delete(id: Long)
}