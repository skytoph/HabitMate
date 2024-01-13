package com.github.skytoph.taski.data.habit.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HabitDB::class, HabitDoneEntryDB::class], version = 1)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}