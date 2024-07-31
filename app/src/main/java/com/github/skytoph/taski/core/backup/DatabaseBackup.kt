package com.github.skytoph.taski.core.backup

import androidx.room.withTransaction
import com.github.skytoph.taski.data.habit.database.HabitDatabase
import com.github.skytoph.taski.data.habit.database.HabitWithEntriesEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface DatabaseBackup {
    suspend fun exportHabits(): ByteArray
    suspend fun importHabits(byteArray: ByteArray)

    class Base(
        private val database: HabitDatabase,
        private val gson: Gson,
        private val compressor: StringCompressor
    ) : DatabaseBackup {

        override suspend fun exportHabits(): ByteArray {
            val entries: List<HabitWithEntriesEntity> = database.entryDao().habitsWithEntries()
            val json = gson.toJson(entries)
            return compressor.compressString(json)
        }

        override suspend fun importHabits(byteArray: ByteArray) {
            val json: String = compressor.decompressString(byteArray)
            val habits: List<HabitWithEntriesEntity> =
                gson.fromJson(json, object : TypeToken<List<HabitWithEntriesEntity>>() {}.type)
            database.withTransaction {
                habits.forEach { habitWithEntries ->
                    database.habitDao().insert(habitWithEntries.habit)
                    habitWithEntries.entries.forEach { entry ->
                        database.entryDao().insert(entry)
                    }
                }
            }
        }
    }
}