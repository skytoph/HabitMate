package com.github.skytoph.taski.data.habit.repository

import com.github.skytoph.taski.data.habit.database.EntriesDao
import com.github.skytoph.taski.data.habit.database.EntryEntity
import com.github.skytoph.taski.data.habit.database.HabitDao
import com.github.skytoph.taski.data.habit.mapper.EntryListMapper
import com.github.skytoph.taski.data.habit.mapper.HabitDBToDomainMapper
import com.github.skytoph.taski.data.habit.mapper.toEntry
import com.github.skytoph.taski.data.habit.mapper.toHabit
import com.github.skytoph.taski.data.habit.mapper.toHabitDB
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.EntryList
import com.github.skytoph.taski.domain.habit.Habit
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BaseHabitRepository(
    private val habitDao: HabitDao,
    private val entryDao: EntriesDao,
    private val habitMapper: HabitDBToDomainMapper,
    private val entryMapper: EntryListMapper
) : HabitRepository {

    override fun habits(): Flow<List<HabitWithEntries>> =
        entryDao.habitsWithEntries().map { list -> list.map { it.map(habitMapper) } }

    override fun entries(id: Long): Flow<EntryList> =
        entryDao.entries(id).map { entries -> entryMapper.map(entries) }

    override suspend fun habit(id: Long): Habit = habitDao.habit(id).toHabit()

    override suspend fun entry(id: Long, timestamp: Long) = entryDao.entry(id, timestamp)?.toEntry()

    override suspend fun insert(habit: Habit) = habitDao.insert(habit.toHabitDB())

    override suspend fun update(habit: Habit) = habitDao.update(habit.toHabitDB())

    override suspend fun insertEntry(id: Long, entry: Entry) =
        entryDao.insert(EntryEntity(id, entry.timestamp, entry.timesDone))

    override suspend fun deleteEntry(id: Long, entry: Entry) =
        entryDao.delete(id, entry.timestamp)

    override suspend fun delete(id: Long) = habitDao.delete(id)
}