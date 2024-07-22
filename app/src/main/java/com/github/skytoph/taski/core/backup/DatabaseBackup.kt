package com.github.skytoph.taski.core.backup

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.room.withTransaction
import com.github.skytoph.taski.data.habit.database.HabitDatabase
import com.github.skytoph.taski.data.habit.database.HabitWithEntriesEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class DatabaseExporter(
    private val database: HabitDatabase,
    private val gson: Gson,
    private val compressor: StringCompressor
) {

    suspend fun exportHabits(context: Context): Uri {
        val entries: List<HabitWithEntriesEntity> = database.entryDao().habitsWithEntries()
        val json = gson.toJson(entries)
        val compressedData = compressor.compressString(json)

        val date = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(System.currentTimeMillis())
        val file = File(context.cacheDir, "backup-$date.db")
        file.writeBytes(compressedData)

        return FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    }

    suspend fun importHabits(contentResolver: ContentResolver, uri: Uri) = withContext(Dispatchers.IO) {
        val byteArray = contentResolver.openInputStream(uri)?.run { readBytes() } ?: return@withContext
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