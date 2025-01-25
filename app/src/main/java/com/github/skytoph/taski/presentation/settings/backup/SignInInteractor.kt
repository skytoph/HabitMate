package com.github.skytoph.taski.presentation.settings.backup

import android.content.Context
import android.content.Intent
import com.github.skytoph.taski.core.NetworkManager
import com.github.skytoph.taski.core.auth.SignInWithGoogle
import com.github.skytoph.taski.core.backup.BackupDatastore
import com.github.skytoph.taski.presentation.core.Logger
import com.github.skytoph.taski.presentation.habit.icon.IconsDatastore

interface SignInInteractor<R> {
    suspend fun signInWithFirebase(intent: Intent, context: Context): R
    suspend fun lastSync(): Long?
    fun checkConnection(context: Context): R

    abstract class Base<R>(
        private val iconsDatastore: IconsDatastore,
        private val networkManager: NetworkManager,
        private val drive: BackupDatastore,
        private val log: Logger
    ) : SignInInteractor<R> {

        abstract val defaultSigningInResult: R
        abstract val noConnectionResult: R
        abstract val connectedResult: R

        override suspend fun signInWithFirebase(intent: Intent, context: Context): R = try {
            val icons = iconsDatastore.unlocked()
            val credentials = SignInWithGoogle.DriveScope.signInWithFirebase(intent)
            credentials?.let {
                iconsDatastore.delete()
                SignInWithGoogle.DriveScope.signInWithCredentials(credentials)
            }
            val profile = SignInWithGoogle.DriveScope.profile()
            run {
                val synchronized = try {
                    if (credentials != null && icons.isNotEmpty()) iconsDatastore.unlock(icons).let { true }
                    else null
                } catch (_: Exception) {
                    false
                }
                mapResult(profile, synchronized)
            }
        } catch (exception: Exception) {
            log.log(exception)
            mapResult(exception, defaultSigningInResult)
        }

        override suspend fun lastSync(): Long? = drive.lastSync()?.value

        override fun checkConnection(context: Context): R = if (networkManager.isNetworkAvailable()) connectedResult
        else noConnectionResult

        abstract fun mapResult(exception: Exception?, default: R): R

        abstract suspend fun mapResult(profile: ProfileUi, synchronized: Boolean?): R
    }
}