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
import com.github.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BaseHabitRepository(
    private val habitDao: HabitDao,
    private val entryDao: EntriesDao,
    private val habitMapper: HabitDBToDomainMapper,
    private val entryMapper: EntryListMapper,
    private val stats: StatisticsUiMapper,
) : HabitRepository {

    override fun entriesFlow(id: Long): Flow<EntryList> =
        entryDao.entriesPagingSource(id).map { entryMapper.map(it) }

    override fun habitsWithEntries(): Flow<List<HabitWithEntries>> =
        entryDao.habitsWithEntriesFlow().map { list -> list.map { it.map(habitMapper) } }

    override fun habitWithEntriesFlow(id: Long): Flow<HabitWithEntries?> =
        entryDao.habitWithEntriesFlow(id).map { it?.map(habitMapper) }

    override suspend fun habits(): List<Habit> =
        habitDao.habits().map { habit -> habit.toHabit() }

    override suspend fun entries(id: Long): EntryList = entryMapper.map(entryDao.entriesList(id))

    override suspend fun habit(id: Long): Habit = habitDao.habit(id).toHabit()

    override suspend fun habitWithEntries(id: Long): HabitWithEntries =
        entryDao.habitWithEntriesById(id).map(habitMapper)

    override fun habitFlow(id: Long): Flow<Habit?> = habitDao.habitFlow(id).map { it?.toHabit() }

    override fun habitsFlow(): Flow<List<Habit>> =
        habitDao.habitsFlow().map { habits -> habits.map { habit -> habit.toHabit() } }

    override suspend fun entry(id: Long, timestamp: Long) = entryDao.entry(id, timestamp)?.toEntry()

    override suspend fun insert(habit: Habit): Long = habitDao.insert(habit.toHabitDB())

    override suspend fun update(habit: Habit) = habitDao.update(habit.toHabitDB())

    override suspend fun insertEntry(id: Long, entry: Entry) =
        entryDao.insert(EntryEntity(id, entry.timestamp, entry.timesDone))

    override suspend fun deleteEntry(id: Long, entry: Entry) =
        entryDao.delete(id, entry.timestamp)

    override suspend fun delete(id: Long) = habitDao.delete(id)

    override suspend fun notCompleted(habitId: Long, isFirstDaySunday: Boolean): Boolean {
        val data = habitWithEntries(habitId)
        return !data.habit.isArchived
                && data.entries.entries[0].let { today -> today == null || today.timesDone < data.habit.goal }
                && stats.state(data, isFirstDaySunday).isScheduledForToday
    }
}