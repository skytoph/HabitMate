package com.github.skytoph.taski.data.habit.repository

import com.github.skytoph.taski.data.habit.database.EntriesDao
import com.github.skytoph.taski.data.habit.database.EntryEntity
import com.github.skytoph.taski.data.habit.database.HabitDao
import com.github.skytoph.taski.data.habit.mapper.HabitDBToDomainMapper
import com.github.skytoph.taski.data.habit.mapper.toEntry
import com.github.skytoph.taski.data.habit.mapper.toHabitDB
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BaseHabitRepository(
    private val habitDao: HabitDao,
    private val entryDao: EntriesDao,
    private val habitMapper: HabitDBToDomainMapper,
) : HabitRepository {

    override fun habits(): Flow<List<Habit>> =
        entryDao.habitsWithEntries().map { list -> list.map { it.map(habitMapper) } }

    override fun habit(id: Long): Flow<Habit> =
        entryDao.habitWithEntriesById(id).map { habit -> habit.map(habitMapper) }

    override suspend fun entry(id: Long, timestamp: Long) = entryDao.entry(id, timestamp)?.toEntry()

    override suspend fun insert(habit: Habit) = habitDao.insert(habit.toHabitDB())

    override suspend fun update(habit: Habit) = habitDao.update(habit.toHabitDB())

    override suspend fun insertEntry(id: Long, entry: Entry) =
        entryDao.insert(EntryEntity(id, entry.timestamp, entry.timesDone))

    override suspend fun deleteEntry(id: Long, entry: Entry) =
        entryDao.delete(id, entry.timestamp)

    override suspend fun delete(id: Long) = habitDao.delete(id)
}