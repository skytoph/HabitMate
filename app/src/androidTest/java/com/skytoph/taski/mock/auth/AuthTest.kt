package com.skytoph.taski.mock.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.skytoph.taski.core.auth.SignInWithGoogle
import com.skytoph.taski.presentation.settings.backup.ProfileUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AuthTest : SignInWithGoogle {

    private val profile = ProfileUi()

    override fun profile(isBackupAvailable: Boolean): ProfileUi =
        profile

    override fun profileFlow(isBackupAvailable: Boolean): Flow<ProfileUi> =
        flowOf(profile)

    override suspend fun signIn(context: Context): GoogleIdTokenCredential? = null

    override suspend fun signInWithCredentials(credentials: AuthCredential) = Unit

    override suspend fun signInWithFirebase(credential: GoogleIdTokenCredential): AuthCredential? = null

    override suspend fun signOut(context: Context) = Unit

    override suspend fun deleteAccount(context: Context, clearData: suspend () -> Unit) = Unit

    override suspend fun signInAnonymously() = Unit

    override suspend fun backupAvailable(context: Context): Boolean = true

    override suspend fun authorizeGoogleDrive(context: Context): AuthorizationResult = result()

    override fun authorizeGoogleDriveResult(context: Context, intent: Intent): AuthorizationResult = result()

    private fun result(): AuthorizationResult = AuthorizationResult(null, null, null, listOf<String>(), null, null)
}