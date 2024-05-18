package com.github.skytoph.taski.data.habit.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HabitEntity::class, EntryEntity::class], version = 3)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun entryDao(): EntriesDao
}