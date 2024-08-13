package com.github.skytoph.taski.presentation.settings.restore

import android.content.Context
import com.github.skytoph.taski.core.backup.BackupDatastore
import com.github.skytoph.taski.core.backup.BackupManager
import com.github.skytoph.taski.core.backup.BackupResult
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.domain.habit.Reminder
import com.github.skytoph.taski.presentation.habit.edit.component.isPermissionNeeded
import com.github.skytoph.taski.presentation.settings.restore.mapper.RestoreBackupResultMapper
import java.util.Locale

interface RestoreInteractor {
    suspend fun backupItems(locale: Locale, context: Context): RestoreResultUi
    suspend fun download(id: String, context: Context): RestoreResultUi
    suspend fun restore(data: ByteArray, context: Context): RestoreResultUi
    suspend fun delete(id: String, locale: Locale, context: Context): RestoreResultUi
    suspend fun deleteAllData(): RestoreResultUi

    class Base(
        private val repository: HabitRepository,
        private val datastore: BackupDatastore,
        private val resultMapper: RestoreBackupResultMapper,
        private val database: BackupManager,
    ) : RestoreInteractor {

        override suspend fun backupItems(locale: Locale, context: Context): RestoreResultUi =
            resultMapper.map(datastore.getFilesList(), locale, context)

        override suspend fun download(id: String, context: Context): RestoreResultUi =
            resultMapper.map(datastore.downloadFile(id), context = context)

        override suspend fun restore(data: ByteArray, context: Context): RestoreResultUi = try {
            database.import(data)
            val containsReminders = repository.habits().find { it.reminder != Reminder.None } != null
            val needsPermission = isPermissionNeeded(context)
            resultMapper.map(BackupResult.Success.FileRestored(containsReminders, needsPermission))
        } catch (exception: Exception) {
            resultMapper.map(BackupResult.Fail.FileNotRestored(exception))
        }

        override suspend fun delete(id: String, locale: Locale, context: Context): RestoreResultUi =
            resultMapper.map(datastore.delete(id), locale, context)

        override suspend fun deleteAllData(): RestoreResultUi {
            val result = datastore.deleteAllFiles()
            return resultMapper.map(result)
        }
    }
}