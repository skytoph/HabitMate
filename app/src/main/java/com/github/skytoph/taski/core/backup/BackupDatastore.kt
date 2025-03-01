package com.github.skytoph.taski.core.backup

import android.accounts.Account
import android.content.Context
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.Logger
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.ByteArrayOutputStream
import java.net.UnknownHostException
import java.util.Collections

interface BackupDatastore {
    suspend fun save(data: ByteArray, filename: String, mime: String): BackupResult
    suspend fun getFilesList(): BackupResult
    suspend fun downloadFile(id: String, restoreSettings: Boolean): BackupResult
    suspend fun delete(id: String): BackupResult
    suspend fun deleteAllFiles(): BackupResult
    suspend fun lastSync(): DateTime?

    class Base(
        private val context: Context,
        private val log: Logger
    ) : BackupDatastore {
        private fun drive(): Drive? {
            val user = Firebase.auth.currentUser ?: return null
            val credential = GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE_APPDATA))
            credential.selectedAccount = Account(user.email ?: return null, "com.google")
            return Drive.Builder(NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(context.getString(R.string.app_name))
                .build()
        }

        override suspend fun save(data: ByteArray, filename: String, mime: String): BackupResult {
            return try {
                val drive = drive() ?: return BackupResult.Fail.DriveIsNotConnected()

                val mediaContent = ByteArrayContent(mime, data)
                val file = File().apply {
                    name = filename
                    setParents(Collections.singletonList("appDataFolder"))
                }
                return drive.files()?.create(file, mediaContent)?.setFields("modifiedTime")?.execute()
                    ?.let { BackupResult.Success.Saved(it.modifiedTime) } ?: BackupResult.Fail.FileNotSaved()
            } catch (exception: Exception) {
                log.log(exception)
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
                log.log(exception)
                BackupResult.Fail.DriveIsNotConnected(exception)
            } catch (exception: Exception) {
                log.log(exception)
                BackupResult.Fail.DriveIsNotConnected(exception)
            }
        }

        override suspend fun downloadFile(id: String, restoreSettings: Boolean): BackupResult {
            return try {
                val drive = drive() ?: return BackupResult.Fail.DriveIsNotConnected()

                val outputStream = ByteArrayOutputStream()
                drive.files()?.get(id)?.executeMediaAndDownloadTo(outputStream)

                BackupResult.Success.FileDownloaded(outputStream.toByteArray(), restoreSettings)
            } catch (exception: Exception) {
                log.log(exception)
                BackupResult.Fail.FileNotDownloaded(exception)
            }
        }

        override suspend fun delete(id: String): BackupResult {
            return try {
                val drive = drive() ?: return BackupResult.Fail.DriveIsNotConnected()

                drive.files()?.delete(id)?.execute()
                val result = drive.files()?.list()?.apply {
                    q =
                        "'appDataFolder' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false"
                    spaces = "appDataFolder"
                    fields = "files(modifiedTime)"
                    orderBy = "modifiedTime desc"
                    pageSize = 1
                }?.execute()?.files?.firstOrNull()?.modifiedTime

                BackupResult.Success.Deleted(
                    time = result,
                    newData = getFiles(drive)?.files ?: emptyList()
                )
            } catch (exception: Exception) {
                log.log(exception)
                BackupResult.Fail.FileNotDownloaded(exception)
            }
        }

        override suspend fun lastSync(): DateTime? {
            return try {
                lastSync(drive() ?: return null)
            } catch (exception: Exception) {
                log.log(exception)
                null
            }
        }

        override suspend fun deleteAllFiles(): BackupResult {
            return try {
                val drive = drive() ?: return BackupResult.Fail.DriveIsNotConnected()

                val result = drive.files()?.list()?.apply {
                    q = "'appDataFolder' in parents"
                    spaces = "appDataFolder"
                    fields = "files(id, parents)"
                }?.execute()

                coroutineScope {
                    result?.files?.map { file -> async { drive.files().delete(file.id).execute() } }?.awaitAll()
                }

                return BackupResult.Success.AllFilesDeleted
            } catch (exception: Exception) {
                log.log(exception)
                BackupResult.Fail.FileNotDeleted(exception)
            }
        }

        private fun getFiles(drive: Drive): FileList? {
            return drive.files()?.list()?.apply {
                q =
                    "'appDataFolder' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false"
                spaces = "appDataFolder"
                fields = "files(id, name, modifiedTime, size, parents, mimeType)"
                orderBy = "modifiedTime desc"
            }?.execute()
        }

        private fun lastSync(drive: Drive): DateTime? = drive.files()?.list()?.apply {
            q = "'appDataFolder' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false"
            spaces = "appDataFolder"
            fields = "files(modifiedTime)"
            orderBy = "modifiedTime desc"
            pageSize = 1
        }?.execute()?.files?.firstOrNull()?.modifiedTime
    }
}