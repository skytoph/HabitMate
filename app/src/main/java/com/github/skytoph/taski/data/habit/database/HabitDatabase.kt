package com.github.skytoph.taski.data.habit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [HabitEntity::class, EntryEntity::class], version = 8)
@TypeConverters(FrequencyConverters::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun entryDao(): EntriesDao
}