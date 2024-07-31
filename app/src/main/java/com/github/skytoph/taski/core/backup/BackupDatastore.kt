package com.github.skytoph.taski.core.backup

import android.content.Context
import android.util.Log
import com.github.skytoph.taski.BuildConfig
import com.github.skytoph.taski.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.ByteArrayOutputStream
import java.net.UnknownHostException
import java.util.Collections

interface BackupDatastore {
    suspend fun save(data: ByteArray, filename: String, mime: String): BackupResult
    suspend fun getFilesList(): BackupResult
    suspend fun downloadFile(id: String): BackupResult
    suspend fun delete(id: String): BackupResult
    suspend fun deleteAllFiles(): BackupResult

    class Base(private val context: Context) : BackupDatastore {
        private fun drive(): Drive? = GoogleSignIn.getLastSignedInAccount(context)?.let { googleAccount ->
            val credential =
                GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE_APPDATA))
            credential.selectedAccount = googleAccount.account

            Drive.Builder(NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(context.getString(R.string.app_name))
                .build()
        }

        override suspend fun save(data: ByteArray, filename: String, mime: String): BackupResult {
            return try {
                val drive = drive() ?: return BackupResult.Fail.DriveIsNotConnected()

                val mediaContent = ByteArrayContent(mime, data)
                val file = File().apply {
                    name = filename
                    setParents(Collections.singletonList(BuildConfig.DRIVE_FOLDER_ID))
                }
                return drive.files()?.create(file, mediaContent)?.setFields("modifiedTime")?.execute()
                    ?.let { BackupResult.Success.Saved(it.modifiedTime) } ?: BackupResult.Fail.FileNotSaved()
            } catch (exception: Exception) {
                Log.e(BackupDatastore::class.simpleName, exception.stackTraceToString())
                BackupResult.Fail.FileNotSaved(exception)
            }
        }

        override suspend fun getFilesList(): BackupResult {
            return try {
                val drive = drive() ?: return BackupResult.Fail.DriveIsNotConnected()
                val result = getFiles(drive)

                return if (result == null) BackupResult.Fail.DriveIsNotConnected()
                else BackupResult.Success.ListOfFiles(result.files ?: emptyList())
            } catch (exception: UnknownHostException) {
                BackupResult.Fail.DriveIsNotConnected(exception)
            } catch (exception: Exception) {
                Log.e(BackupDatastore::class.simpleName, exception.stackTraceToString())
                BackupResult.Fail.DriveIsNotConnected(exception)
            }
        }

        private fun getFiles(drive: Drive): FileList? {
            val parentFolderId = BuildConfig.DRIVE_FOLDER_ID

            return drive.files()?.list()?.apply {
                q =
                    "'$parentFolderId' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false"
                spaces = parentFolderId
                fields = "files(id, name, modifiedTime, size, parents, mimeType)"
                orderBy = "modifiedTime desc"
            }?.execute()
        }

        override suspend fun downloadFile(id: String): BackupResult {
            return try {
                val drive = drive() ?: return BackupResult.Fail.DriveIsNotConnected()

                val outputStream = ByteArrayOutputStream()
                drive.files()?.get(id)?.executeMediaAndDownloadTo(outputStream)

                BackupResult.Success.FileDownloaded(outputStream.toByteArray())
            } catch (exception: Exception) {
                Log.e(BackupDatastore::class.simpleName, exception.stackTraceToString())
                BackupResult.Fail.FileNotDownloaded(exception)
            }
        }

        override suspend fun delete(id: String): BackupResult {
            return try {
                val drive = drive() ?: return BackupResult.Fail.DriveIsNotConnected()

                val parentFolderId = BuildConfig.DRIVE_FOLDER_ID

                drive.files()?.delete(id)?.execute()
                val result = drive.files()?.list()?.apply {
                    q =
                        "'$parentFolderId' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false"
                    spaces = parentFolderId
                    fields = "files(modifiedTime)"
                    orderBy = "modifiedTime desc"
                    pageSize = 1
                }?.execute()

                BackupResult.Success.Deleted(
                    time = result?.files?.firstOrNull()?.modifiedTime,
                    newData = getFiles(drive)?.files ?: emptyList()
                )
            } catch (exception: Exception) {
                Log.e(BackupDatastore::class.simpleName, exception.stackTraceToString())
                BackupResult.Fail.FileNotDownloaded(exception)
            }
        }

        override suspend fun deleteAllFiles(): BackupResult {
            return try {
                val drive = drive() ?: return BackupResult.Fail.DriveIsNotConnected()

                val parentFolderId = BuildConfig.DRIVE_FOLDER_ID

                val result = drive.files()?.list()?.apply {
                    q = "'$parentFolderId' in parents"
                    spaces = parentFolderId
                    fields = "files(id, parents)"
                }?.execute()

                coroutineScope {
                    result?.files?.map { file -> async { drive.files().delete(file.id).execute() } }?.awaitAll()
                }

                return BackupResult.Success.AllFilesDeleted
            } catch (exception: Exception) {
                Log.e(BackupDatastore::class.simpleName, exception.stackTraceToString())
                BackupResult.Fail.FileNotDeleted(exception)
            }
        }
    }
}