package com.github.skytoph.taski.data.habit.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface EntriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: EntryEntity)

    @Query("DELETE FROM entry WHERE habit_id = :id AND timestamp = :timestamp")
    suspend fun delete(id: Long, timestamp: Long)

    @Query("SELECT * FROM entry WHERE habit_id = :id AND timestamp = :timestamp")
    fun entry(id: Long, timestamp: Long): EntryEntity?

    @Query("SELECT * FROM entry WHERE habit_id = :id")
    fun entries(id: Long): Flow<List<EntryEntity>>

    @Query("SELECT * FROM entry WHERE habit_id = :id")
    suspend fun entriesList(id: Long): List<EntryEntity>

    @Transaction
    @Query("SELECT * FROM habit")
    fun habitsWithEntries(): Flow<List<HabitWithEntriesEntity>>

    @Transaction
    @Query("SELECT * FROM habit WHERE id = :id")
    fun habitWithEntriesById(id: Long): Flow<HabitWithEntriesEntity>
}