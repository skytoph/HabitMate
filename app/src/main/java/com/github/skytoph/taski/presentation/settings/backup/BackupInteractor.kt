package com.github.skytoph.taski.presentation.settings.backup

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.NetworkManager
import com.github.skytoph.taski.core.auth.SignInWithGoogle
import com.github.skytoph.taski.core.backup.BackupDatastore
import com.github.skytoph.taski.core.backup.BackupManager
import com.github.skytoph.taski.core.backup.BackupResult
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.domain.habit.Reminder
import com.github.skytoph.taski.presentation.core.Logger
import com.github.skytoph.taski.presentation.core.NetworkErrorMapper
import com.github.skytoph.taski.presentation.habit.edit.component.isPermissionNeeded
import com.github.skytoph.taski.presentation.habit.icon.IconsDatastore
import com.github.skytoph.taski.presentation.settings.backup.mapper.BackupFilename
import com.github.skytoph.taski.presentation.settings.backup.mapper.BackupResultMapper
import com.github.skytoph.taski.presentation.settings.backup.mapper.FileToUri
import java.text.SimpleDateFormat
import java.util.Locale


interface BackupInteractor : SignInInteractor<BackupResultUi> {
    suspend fun saveBackupOnDrive(context: Context): BackupResultUi
    suspend fun profile(context: Context): BackupResultUi
    suspend fun signOut(context: Context): BackupResultUi
    suspend fun deleteAccount(context: Context): BackupResultUi
    suspend fun clear(): BackupResultUi
    fun mapTime(lastBackupSaved: Long?, loading: Long, context: Context, locale: Locale): String?
    suspend fun export(context: Context): BackupResultUi
    suspend fun import(contentResolver: ContentResolver, uri: Uri, context: Context, restoreSettings: Boolean)
            : BackupResultUi

    class Base(
        private val repository: HabitRepository,
        private val backup: BackupManager,
        private val drive: BackupDatastore,
        private val fileWriter: FileToUri,
        private val mapper: BackupResultMapper,
        private val iconsDatastore: IconsDatastore,
        private val networkMapper: NetworkErrorMapper,
        private val log: Logger,
        networkManager: NetworkManager
    ) : BackupInteractor, SignInInteractor.Base<BackupResultUi>(iconsDatastore, networkManager, drive, log) {

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
            SignInWithGoogle.DriveScope.profile().let {
                BackupResultUi.Success.ProfileLoaded(it)
            }

        override suspend fun signOut(context: Context): BackupResultUi = try {
            SignInWithGoogle.DriveScope.signOut(context)
            profile(context)
        } catch (exception: Exception) {
            log.log(exception)
            mapResult(exception, BackupResultUi.SignOutFailed)
        }

        override suspend fun deleteAccount(context: Context): BackupResultUi = try {
            val result = drive.deleteAllFiles()
            iconsDatastore.delete()
            SignInWithGoogle.DriveScope.deleteAccount(context)
            BackupResultUi.DeletingAccount(deleted = result is BackupResult.Success)
        } catch (exception: Exception) {
            log.log(exception)
            mapResult(exception, BackupResultUi.DeletingAccount(deleted = false))
        }

        override fun mapTime(lastBackupSaved: Long?, loading: Long, context: Context, locale: Locale): String? =
            when (lastBackupSaved) {
                loading -> null
                null -> context.getString(R.string.no_backup_data)
                else -> context.getString(
                    R.string.last_time_backup_saved,
                    SimpleDateFormat(context.getString(R.string.backup_date_format), locale).format(lastBackupSaved)
                )
            }

        override suspend fun mapResult(profile: ProfileUi, synchronized: Boolean?): BackupResultUi =
            BackupResultUi.Success.SignedIn(profile, lastSync(), synchronized)

        override fun mapResult(exception: Exception?, default: BackupResultUi): BackupResultUi = when {
            exception != null && networkMapper.isNetworkUnavailable(exception) -> BackupResultUi.NoConnection
            else -> default
        }

        override val defaultSigningInResult: BackupResultUi = BackupResultUi.SignInFailed
        override val noConnectionResult: BackupResultUi = BackupResultUi.NoConnection
        override val connectedResult: BackupResultUi = BackupResultUi.Empty
    }
}