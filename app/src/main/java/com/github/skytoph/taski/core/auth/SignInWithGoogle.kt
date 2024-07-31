package com.github.skytoph.taski.core.auth

import android.content.Context
import com.github.skytoph.taski.presentation.settings.backup.ProfileUi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.tasks.await

interface SignInWithGoogle<C> {
    fun getClient(context: Context): C
    fun profile(context: Context): ProfileUi?
    suspend fun signOut(context: Context)

    object DriveScope : SignInWithGoogle<GoogleSignInClient> {
        override fun getClient(context: Context): GoogleSignInClient {
            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestScopes(Scope(DriveScopes.DRIVE_APPDATA))
                .build()

            return GoogleSignIn.getClient(context, signInOptions)
        }

        override suspend fun signOut(context: Context) {
            GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut().await()
        }

        override fun profile(context: Context): ProfileUi? {
            val account = GoogleSignIn.getLastSignedInAccount(context)
            return if (account == null) null else ProfileUi(
                email = account.email ?: "",
                name = account.displayName ?: "",
                imageUri = account.photoUrl
            )
        }
    }
}