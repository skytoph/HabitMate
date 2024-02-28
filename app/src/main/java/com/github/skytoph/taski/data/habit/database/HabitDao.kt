package com.github.skytoph.taski.data.habit.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity)

    @Update
    suspend fun update(habit: HabitEntity)

    @Query("SELECT * FROM habit WHERE id = :id")
    suspend fun habit(id: Long): HabitEntity

    @Query("SELECT * FROM habit WHERE id = :id")
    fun habitFlow(id: Long): Flow<HabitEntity>

    @Query("DELETE FROM habit WHERE id = :id")
    suspend fun delete(id: Long)
}