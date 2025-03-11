package com.skytoph.taski.presentation.settings.restore

import android.content.Context
import com.skytoph.taski.core.backup.BackupDatastore
import com.skytoph.taski.core.backup.BackupManager
import com.skytoph.taski.core.backup.BackupResult
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.domain.habit.Reminder
import com.skytoph.taski.presentation.core.Logger
import com.skytoph.taski.presentation.habit.edit.component.isPermissionNeeded
import com.skytoph.taski.presentation.settings.restore.mapper.RestoreBackupResultMapper
import java.util.Locale

interface RestoreInteractor {
    suspend fun backupItems(locale: Locale, context: Context, is24HoursFormat: Boolean): RestoreResultUi
    suspend fun delete(id: String, locale: Locale, context: Context, is24HoursFormat: Boolean): RestoreResultUi
    suspend fun deleteAllData(): RestoreResultUi
    suspend fun download(id: String, restoreSettings: Boolean, context: Context, is24HoursFormat: Boolean)
            : RestoreResultUi

    suspend fun restore(data: ByteArray, context: Context, restoreSettings: Boolean, is24HoursFormat: Boolean)
            : RestoreResultUi

    class Base(
        private val repository: HabitRepository,
        private val datastore: BackupDatastore,
        private val resultMapper: RestoreBackupResultMapper,
        private val database: BackupManager,
        private val log: Logger
    ) : RestoreInteractor {

        override suspend fun backupItems(locale: Locale, context: Context, is24HoursFormat: Boolean)
                : RestoreResultUi =
            resultMapper.map(datastore.getFilesList(), is24HoursFormat, locale, context)

        override suspend fun download(id: String, restoreSettings: Boolean, context: Context, is24HoursFormat: Boolean)
                : RestoreResultUi =
            resultMapper.map(datastore.downloadFile(id, restoreSettings), is24HoursFormat, context = context)

        override suspend fun restore(
            data: ByteArray, context: Context, restoreSettings: Boolean, is24HoursFormat: Boolean
        ): RestoreResultUi = try {
            database.import(data, restoreSettings)
            val containsReminders = repository.habits().find { it.reminder != Reminder.None } != null
            val needsPermission = isPermissionNeeded(context)
            resultMapper.map(BackupResult.Success.FileRestored(containsReminders, needsPermission), is24HoursFormat)
        } catch (exception: Exception) {
            log.log(exception)
            resultMapper.map(BackupResult.Fail.FileNotRestored(exception), is24HoursFormat)
        }

        override suspend fun delete(id: String, locale: Locale, context: Context, is24HoursFormat: Boolean)
                : RestoreResultUi = resultMapper.map(datastore.delete(id), is24HoursFormat, locale, context)

        override suspend fun deleteAllData(): RestoreResultUi {
            val result = datastore.deleteAllFiles()
            return resultMapper.map(result, false)
        }
    }
}