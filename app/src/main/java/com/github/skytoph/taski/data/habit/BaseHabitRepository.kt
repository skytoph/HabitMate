package com.github.skytoph.taski.data.habit

import com.github.skytoph.taski.data.habit.database.EntriesDao
import com.github.skytoph.taski.data.habit.database.EntryEntity
import com.github.skytoph.taski.data.habit.database.HabitDao
import com.github.skytoph.taski.data.habit.mapper.toHabit
import com.github.skytoph.taski.data.habit.mapper.toHabitDB
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BaseHabitRepository(
    private val habitDao: HabitDao,
    private val entryDao: EntriesDao,
) : HabitRepository {

    override fun habits(): Flow<List<Habit>> =
        entryDao.habitsWithEntries().map { list -> list.map { it.toHabit() } }

    override fun habit(id: Long): Flow<Habit> = habitDao.habit(id).map { it.toHabit() }

    override suspend fun insert(habit: Habit) = habitDao.insert(habit.toHabitDB())

    override suspend fun insertEntry(id: Long, entry: Entry) =
        entryDao.insert(EntryEntity(id, entry.timestamp, entry.timesDone))

    override suspend fun deleteEntry(id: Long, entry: Entry) =
        entryDao.delete(id, entry.timestamp)

    override suspend fun delete(id: Long) = habitDao.delete(id)
}