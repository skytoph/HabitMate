package com.github.skytoph.taski.core.backup

import com.google.api.client.util.DateTime
import com.google.api.services.drive.model.File

sealed interface BackupResult {
    sealed interface Success : BackupResult {
        class ListOfFiles(val data: List<File>) : Success
        class FileDownloaded(val file: ByteArray) : Success
        class Saved(val time: DateTime) : Success
        class Deleted(val time: DateTime?, val newData: List<File>) : Success
        class FileRestored(val containsReminders: Boolean) : Success
        data object AllFilesDeleted : Success
    }

    sealed class Fail(val exception: Exception? = null) : BackupResult {
        class FileNotSaved(exception: Exception? = null) : Fail(exception)
        class FileNotDownloaded(exception: Exception? = null) : Fail(exception)
        class FileNotDeleted(exception: Exception? = null) : Fail(exception)
        class DriveIsNotConnected(exception: Exception? = null) : Fail(exception)
        class FileNotRestored(exception: Exception? = null) : Fail(exception)
    }
}