package com.github.skytoph.taski.data.habit.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habit")
    fun habits(): Flow<List<HabitDB>>

    @Query("SELECT * FROM habit WHERE id = :id")
    fun habit(id: Long): HabitDB

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(habit: HabitDB)

    @Query("DELETE FROM habit WHERE id = :id")
    fun delete(id: Long)
}