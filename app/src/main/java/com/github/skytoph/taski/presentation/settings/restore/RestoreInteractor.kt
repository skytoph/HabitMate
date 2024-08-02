package com.github.skytoph.taski.presentation.settings.restore

import android.content.Context
import com.github.skytoph.taski.core.backup.BackupDatastore
import com.github.skytoph.taski.core.backup.BackupManager
import com.github.skytoph.taski.core.backup.BackupResult
import com.github.skytoph.taski.presentation.settings.restore.mapper.RestoreBackupResultMapper
import java.util.Locale

interface RestoreInteractor {
    suspend fun backupItems(locale: Locale, context: Context): RestoreResultUi
    suspend fun download(id: String): RestoreResultUi
    suspend fun restore(data: ByteArray): RestoreResultUi
    suspend fun delete(id: String, locale: Locale, context: Context): RestoreResultUi
    suspend fun deleteAllData(): RestoreResultUi

    class Base(
        private val datastore: BackupDatastore,
        private val resultMapper: RestoreBackupResultMapper,
        private val database: BackupManager,
    ) : RestoreInteractor {

        override suspend fun backupItems(locale: Locale, context: Context): RestoreResultUi =
            resultMapper.map(datastore.getFilesList(), locale, context)

        override suspend fun download(id: String): RestoreResultUi = resultMapper.map(datastore.downloadFile(id))

        override suspend fun restore(data: ByteArray): RestoreResultUi = try {
            database.import(data)
            resultMapper.map(BackupResult.Success.FileRestored)
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