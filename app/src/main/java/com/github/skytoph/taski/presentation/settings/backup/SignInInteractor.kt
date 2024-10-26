package com.github.skytoph.taski.presentation.settings.backup

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.github.skytoph.taski.core.auth.SignInWithGoogle
import com.github.skytoph.taski.presentation.habit.icon.IconsDatastore

interface SignInInteractor<R> {
    suspend fun signInWithFirebase(intent: Intent, context: Context): R
    fun checkConnection(context: Context): R

    abstract class Base<R>(
        private val iconsDatastore: IconsDatastore,
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
            if (profile == null) mapResult(null, defaultSigningInResult)
            else {
                val synchronized = try {
                    if (credentials != null && icons.isNotEmpty()) iconsDatastore.unlock(icons).let { true }
                    else null
                } catch (_: Exception) {
                    false
                }
                mapResult(profile, synchronized)
            }
        } catch (exception: Exception) {
            Log.e("tag", exception.stackTraceToString())
            mapResult(exception, defaultSigningInResult)
        }

        override fun checkConnection(context: Context): R {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities = connectivityManager.activeNetwork?.let { connectivityManager.getNetworkCapabilities(it) }
            return if (capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
            ) connectedResult
            else noConnectionResult
        }

        abstract fun mapResult(exception: Exception?, default: R): R

        abstract suspend fun mapResult(profile: ProfileUi, synchronized: Boolean?): R
    }
}