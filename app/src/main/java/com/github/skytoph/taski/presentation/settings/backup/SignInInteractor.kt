package com.github.skytoph.taski.presentation.settings.backup

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.github.skytoph.taski.core.NetworkManager
import com.github.skytoph.taski.core.auth.SignInWithGoogle
import com.github.skytoph.taski.core.backup.BackupDatastore
import com.github.skytoph.taski.presentation.core.Logger
import com.github.skytoph.taski.presentation.habit.icon.IconsDatastore
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

interface SignInInteractor<R> {
    suspend fun signInWithFirebase(context: Context, tokenCredential: GoogleIdTokenCredential, permissionNeeded: Boolean): R
    suspend fun signIn(context: Context, permissionNeeded: Boolean): R
    suspend fun lastSync(): Long?
    suspend fun authorizeResult(context: Context, intent: Intent?)
    suspend fun backupPermissionRequest(context: Context): IntentSender?
    suspend fun mapResult(profile: ProfileUi, synchronized: Boolean?, permissionNeeded: Boolean): R
    fun checkConnection(context: Context): R
    fun mapResult(exception: Exception?, default: R): R

    abstract class Base<R>(
        private val iconsDatastore: IconsDatastore,
        private val networkManager: NetworkManager,
        private val drive: BackupDatastore,
        private val auth: SignInWithGoogle,
        private val log: Logger
    ) : SignInInteractor<R> {

        abstract val defaultSignInFailResult: R
        abstract val noConnectionResult: R
        abstract val connectedResult: R
        abstract val emptyResult: R

        override suspend fun signInWithFirebase(
            context: Context, tokenCredential: GoogleIdTokenCredential, permissionNeeded: Boolean
        ): R = try {
            val oldProfile = auth.profile()
            val icons = iconsDatastore.unlocked(oldProfile)
            val credentials = auth.signInWithFirebase(tokenCredential)
            credentials?.let {
                iconsDatastore.delete(oldProfile)
                auth.signInWithCredentials(credentials)
            }
            val profile = auth.profile(auth.backupAvailable(context))
            val synchronized = try {
                if (credentials != null && icons.isNotEmpty()) iconsDatastore.unlock(profile, icons).let { true }
                else null
            } catch (_: Exception) {
                false
            }
            mapResult(profile, synchronized, permissionNeeded && !auth.backupAvailable(context))
        } catch (exception: Exception) {
            log.log(exception)
            mapResult(exception, defaultSignInFailResult)
        }

        override suspend fun signIn(context: Context, permissionNeeded: Boolean): R {
            val credentials = auth.signIn(context)
            return if (credentials != null) signInWithFirebase(context, credentials, permissionNeeded)
            else defaultSignInFailResult
        }

        override suspend fun authorizeResult(context: Context, intent: Intent?) {
            if (intent != null) auth.authorizeGoogleDriveResult(context, intent)
        }

        override suspend fun backupPermissionRequest(context: Context): IntentSender? =
            auth.authorizeGoogleDrive(context).let { result ->
                if (result.hasResolution()) result.pendingIntent?.intentSender else null
            }

        override suspend fun lastSync(): Long? = drive.lastSync()?.value

        override fun checkConnection(context: Context): R = if (networkManager.isNetworkAvailable()) connectedResult
        else noConnectionResult
    }
}