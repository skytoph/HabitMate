package com.github.skytoph.taski.core.backup

import androidx.annotation.Keep
import androidx.room.withTransaction
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.core.datastore.settings.Settings
import com.github.skytoph.taski.data.habit.database.HabitDatabase
import com.github.skytoph.taski.data.habit.database.HabitWithEntriesEntity
import com.google.gson.Gson
import kotlinx.coroutines.flow.first

interface BackupManager {
    suspend fun export(): ByteArray
    suspend fun import(byteArray: ByteArray, restoreSettings: Boolean)
    suspend fun clear()

    class Base(
        private val database: HabitDatabase,
        private val gson: Gson,
        private val compressor: StringCompressor,
        private val settings: SettingsCache
    ) : BackupManager {

        override suspend fun export(): ByteArray {
            val entries: List<HabitWithEntriesEntity> = database.entryDao().habitsWithEntries()
            val backup = Backup(habitsWithEntries = entries, settings = settings.initAndGet().first())
            val json = gson.toJson(backup)
            return compressor.compressString(json)
        }

        override suspend fun import(byteArray: ByteArray, restoreSettings: Boolean) {
            val json: String = compressor.decompressString(byteArray)
            val backup: Backup = gson.fromJson(json, Backup::class.java)
            val habits: List<HabitWithEntriesEntity> = backup.habitsWithEntries
            if (restoreSettings) settings.update(backup.settings)
            database.withTransaction {
                database.habitDao().deleteAll()
                habits.forEach { habitWithEntries ->
                    database.habitDao().insert(habitWithEntries.habit)
                    habitWithEntries.entries.forEach { entry ->
                        database.entryDao().insert(entry)
                    }
                }
            }
        }

        override suspend fun clear() {
            database.habitDao().deleteAll()
        }
    }
}

@Keep
data class Backup(
    val habitsWithEntries: List<HabitWithEntriesEntity>,
    val settings: Settings,
)