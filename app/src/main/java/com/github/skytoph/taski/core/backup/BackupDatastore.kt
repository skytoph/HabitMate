package com.github.skytoph.taski.core.backup

import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import java.io.ByteArrayOutputStream

interface BackupDatastore {
    suspend fun save(file: File)
    suspend fun getFilesList(): List<File>
    suspend fun downloadFile(id: String): ByteArray

    class Base(private val drive: Drive?) : BackupDatastore {
        override suspend fun save(file: File) {
            if (drive == null) return
            drive.files().create(file).execute()
        }

        override suspend fun getFilesList(): List<File> {
            if (drive == null) return emptyList()
            val result = drive.files().list().apply {
                q = "mimeType='application/vnd.google-apps.spreadsheet'"
                spaces = "drive"
                fields = "files(id, name, modifiedTime, size)"
                orderBy = "modifiedTime desc"
            }.execute()
            val files: List<File> = result.files
            return files
        }

        override suspend fun downloadFile(id: String): ByteArray {
            val outputStream = ByteArrayOutputStream()
            drive?.files()?.get(id)?.executeMediaAndDownloadTo(outputStream)
            return outputStream.toByteArray() ?: ByteArray(0)
        }
    }
}

data class BackupFile(val id: String, val modifiedTime: Long, val size: Long)

fun File.map(): BackupFile = BackupFile(
    id = id,
    modifiedTime = modifiedTime.value,
    size = getSize()
)