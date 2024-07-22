package com.github.skytoph.taski.presentation.settings.backup

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import com.github.skytoph.taski.core.backup.DatabaseExporter

interface BackupInteractor {
    suspend fun export(context: Context): Uri?
    suspend fun import(contentResolver: ContentResolver, uri: Uri): Boolean

    class Base(private val backup: DatabaseExporter) : BackupInteractor {

        override suspend fun export(context: Context): Uri? = try {
            backup.exportHabits(context)
        } catch (exception: Exception) {
            Log.e("tag", exception.stackTraceToString())
            null
        }

        override suspend fun import(contentResolver: ContentResolver, uri: Uri) = try {
            backup.importHabits(contentResolver, uri)
            true
        } catch (exception: Exception) {
            Log.e("tag", exception.stackTraceToString())
            false
        }
    }
}
