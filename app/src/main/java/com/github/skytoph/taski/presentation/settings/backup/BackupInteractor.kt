package com.github.skytoph.taski.presentation.settings.backup

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.auth.SignInWithGoogle
import com.github.skytoph.taski.core.backup.BackupDatastore
import com.github.skytoph.taski.core.backup.BackupManager
import com.github.skytoph.taski.core.backup.BackupResult
import com.github.skytoph.taski.presentation.core.NetworkErrorMapper
import com.github.skytoph.taski.presentation.settings.backup.mapper.BackupFilename
import com.github.skytoph.taski.presentation.settings.backup.mapper.BackupResultMapper
import com.github.skytoph.taski.presentation.settings.backup.mapper.FileToUri
import java.text.SimpleDateFormat
import java.util.Locale


interface BackupInteractor {
    suspend fun export(context: Context): BackupResultUi
    suspend fun import(contentResolver: ContentResolver, uri: Uri): BackupResultUi
    suspend fun saveBackupOnDrive(context: Context): BackupResultUi
    suspend fun profile(context: Context): BackupResultUi
    suspend fun signOut(context: Context)
    suspend fun deleteAccount(context: Context): BackupResultUi
    suspend fun signInWithFirebase(intent: Intent, context: Context): BackupResultUi
    fun checkConnection(context: Context): BackupResultUi
    fun mapTime(lastBackupSaved: Long?, loading: Long, context: Context, locale: Locale): String?

    class Base(
        private val backup: BackupManager,
        private val drive: BackupDatastore,
        private val fileWriter: FileToUri,
        private val mapper: BackupResultMapper,
        private val networkMapper: NetworkErrorMapper
    ) : BackupInteractor {

        override suspend fun export(context: Context): BackupResultUi = try {
            val uri = fileWriter.getUriFromFile(
                file = fileWriter.writeToFile(
                    byteArray = backup.export(), filename = BackupFilename.generate(context), context = context
                ), context = context
            )
            if (uri != null) BackupResultUi.Success.BackupExported(uri)
            else BackupResultUi.ExportFailed
        } catch (exception: Exception) {
            Log.e("tag", exception.stackTraceToString())
            BackupResultUi.ExportFailed
        }

        override suspend fun import(contentResolver: ContentResolver, uri: Uri): BackupResultUi {
            return try {
                val data = fileWriter.readFromUri(contentResolver, uri) ?: return BackupResultUi.Imported(false)
                backup.import(data)
                BackupResultUi.Imported(true)
            } catch (exception: Exception) {
                Log.e("tag", exception.stackTraceToString())
                BackupResultUi.Imported(false)
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

        override suspend fun signInWithFirebase(intent: Intent, context: Context): BackupResultUi = try {
            SignInWithGoogle.DriveScope.signInWithFirebase(intent)
            val profile = SignInWithGoogle.DriveScope.profile(context)
            if (profile == null) BackupResultUi.SignInFailed
            else BackupResultUi.Success.SignedIn(profile)
        } catch (exception: Exception) {
            if (networkMapper.isNetworkUnavailable(exception)) BackupResultUi.NoConnection
            else BackupResultUi.SignInFailed
        }

        override suspend fun profile(context: Context): BackupResultUi =
            SignInWithGoogle.DriveScope.profile(context).let {
                if (it != null) BackupResultUi.Success.ProfileLoaded(it)
                else BackupResultUi.ProfileLoadingFailed
            }

        override suspend fun signOut(context: Context) = SignInWithGoogle.DriveScope.signOut(context)

        override suspend fun deleteAccount(context: Context): BackupResultUi {
            val result = drive.deleteAllFiles()
            SignInWithGoogle.DriveScope.signOut(context)
            return BackupResultUi.DeletingAccount(deleted = result is BackupResult.Success)
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

        override fun checkConnection(context: Context): BackupResultUi {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities = connectivityManager.activeNetwork?.let { connectivityManager.getNetworkCapabilities(it) }
            return if (capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
            ) BackupResultUi.Empty
            else BackupResultUi.NoConnection
        }
    }
}