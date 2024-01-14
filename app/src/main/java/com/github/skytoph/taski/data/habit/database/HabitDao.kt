package com.github.skytoph.taski.data.habit.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habit")
    fun habits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habit WHERE id = :id")
    suspend fun habit(id: Long): HabitEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity)

    @Query("DELETE FROM habit WHERE id = :id")
    suspend fun delete(id: Long)
}