package com.skytoph.taski.presentation.settings.backup

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.skytoph.taski.R
import com.skytoph.taski.core.NetworkManager
import com.skytoph.taski.core.auth.SignInWithGoogle
import com.skytoph.taski.core.backup.BackupDatastore
import com.skytoph.taski.core.backup.BackupManager
import com.skytoph.taski.core.backup.BackupResult
import com.skytoph.taski.domain.habit.HabitRepository
import com.skytoph.taski.domain.habit.Reminder
import com.skytoph.taski.presentation.core.Logger
import com.skytoph.taski.presentation.core.NetworkErrorMapper
import com.skytoph.taski.presentation.habit.edit.component.isPermissionNeeded
import com.skytoph.taski.presentation.habit.icon.IconsDatastore
import com.skytoph.taski.presentation.settings.backup.mapper.BackupFilename
import com.skytoph.taski.presentation.settings.backup.mapper.BackupResultMapper
import com.skytoph.taski.presentation.settings.backup.mapper.FileToUri
import java.text.SimpleDateFormat
import java.util.Locale


interface BackupInteractor : SignInInteractor<BackupResultUi> {
    suspend fun saveBackupOnDrive(context: Context): BackupResultUi
    suspend fun profile(context: Context): BackupResultUi
    suspend fun signOut(context: Context): BackupResultUi
    suspend fun deleteAccount(context: Context): BackupResultUi
    suspend fun clear(): BackupResultUi
    fun log(data: Intent?)
    suspend fun export(context: Context): BackupResultUi
    suspend fun import(contentResolver: ContentResolver, uri: Uri, context: Context, restoreSettings: Boolean)
            : BackupResultUi

    fun mapTime(lastBackupSaved: Long?, loading: Long, context: Context, locale: Locale, is24HoursFormat: Boolean)
            : String?

    class Base(
        private val repository: HabitRepository,
        private val backup: BackupManager,
        private val drive: BackupDatastore,
        private val fileWriter: FileToUri,
        private val mapper: BackupResultMapper,
        private val iconsDatastore: IconsDatastore,
        private val networkMapper: NetworkErrorMapper,
        private val log: Logger,
        private val auth: SignInWithGoogle,
        networkManager: NetworkManager
    ) : BackupInteractor, SignInInteractor.Base<BackupResultUi>(iconsDatastore, networkManager, drive, auth, log) {

        override suspend fun export(context: Context): BackupResultUi = try {
            val uri = fileWriter.getUriFromFile(
                file = fileWriter.writeToFile(
                    byteArray = backup.export(), filename = BackupFilename.generate(context), context = context
                ), context = context
            )
            if (uri != null) BackupResultUi.Success.BackupExported(uri)
            else BackupResultUi.ExportFailed
        } catch (exception: Exception) {
            log.log(exception)
            BackupResultUi.ExportFailed
        }

        override suspend fun import(
            contentResolver: ContentResolver, uri: Uri, context: Context, restoreSettings: Boolean
        ): BackupResultUi {
            return try {
                val data = fileWriter.readFromUri(contentResolver, uri) ?: return BackupResultUi.Imported(false)
                backup.import(data, restoreSettings)
                val containsReminders = repository.habits().find { it.reminder != Reminder.None } != null
                val needsPermission = isPermissionNeeded(context)
                BackupResultUi.Imported(true, containsReminders, needsPermission)
            } catch (exception: Exception) {
                log.log(exception)
                BackupResultUi.Imported(false)
            }
        }

        override suspend fun clear(): BackupResultUi {
            return try {
                backup.clear()
                BackupResultUi.ClearData(true)
            } catch (exception: Exception) {
                log.log(exception)
                BackupResultUi.ClearData(false)
            }
        }

        override suspend fun saveBackupOnDrive(context: Context): BackupResultUi {
            val result = drive.save(
                data = backup.export(),
                filename = BackupFilename.generate(context),
                mime = context.getString(R.string.backup_mimetype)
            )
            return mapper.map(result)
        }

        override suspend fun profile(context: Context): BackupResultUi =
            auth.profile(auth.backupAvailable(context)).let {
                BackupResultUi.Success.ProfileLoaded(it)
            }

        override suspend fun signOut(context: Context): BackupResultUi = try {
            auth.signOut(context)
            profile(context)
        } catch (exception: Exception) {
            log.log(exception)
            mapResult(exception, BackupResultUi.SignOutFailed)
        }

        override suspend fun deleteAccount(context: Context): BackupResultUi = try {
            val result = drive.deleteAllFiles()
            iconsDatastore.delete(auth.profile())
            auth.deleteAccount(context)
            BackupResultUi.DeletingAccount(deleted = result is BackupResult.Success)
            BackupResultUi.DeletingAccount(deleted = true)
        } catch (exception: Exception) {
            log.log(exception)
            mapResult(exception, BackupResultUi.DeletingAccount(deleted = false))
        }

        override fun mapTime(
            lastBackupSaved: Long?, loading: Long, context: Context, locale: Locale, is24HoursFormat: Boolean
        ): String? = when (lastBackupSaved) {
            loading -> null
            null -> context.getString(R.string.no_backup_data)
            else -> {
                val dateFormat =
                    context.getString(if (is24HoursFormat) R.string.backup_date_format_24h_format else R.string.backup_date_format_12h_format)
                context.getString(
                    R.string.last_time_backup_saved, SimpleDateFormat(dateFormat, locale).format(lastBackupSaved)
                )
            }
        }

        override suspend fun mapResult(
            profile: ProfileUi, synchronized: Boolean?, permissionNeeded: Boolean
        ): BackupResultUi = BackupResultUi.Success.SignedIn(profile, lastSync(), synchronized, permissionNeeded)

        override fun mapResult(exception: Exception?, default: BackupResultUi): BackupResultUi = when {
            exception != null && networkMapper.isNetworkUnavailable(exception) -> BackupResultUi.NoConnection
            else -> default
        }

        override fun log(data: Intent?) {
            val message = data?.extras?.apply {
                keySet()?.joinToString(separator = "\n") { key -> "Key [$key]: ${get(key)?.toString()}" }
            }
            log.log("Google sign in failed.\n$message")
        }

        override val defaultSignInFailResult: BackupResultUi = BackupResultUi.SignInFailed
        override val noConnectionResult: BackupResultUi = BackupResultUi.NoConnection
        override val connectedResult: BackupResultUi = BackupResultUi.Empty
        override val emptyResult: BackupResultUi = BackupResultUi.Empty
    }
}