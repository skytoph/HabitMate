package com.github.skytoph.taski.core.auth

import android.content.Context
import android.content.Intent
import com.github.skytoph.taski.BuildConfig
import com.github.skytoph.taski.presentation.settings.backup.ProfileUi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface SignInWithGoogle<C> {
    fun getClient(context: Context): C
    fun profile(context: Context): ProfileUi?
    suspend fun signInWithFirebase(intent: Intent)
    suspend fun signOut(context: Context)

    object DriveScope : SignInWithGoogle<GoogleSignInClient> {
        override fun getClient(context: Context): GoogleSignInClient {
            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(BuildConfig.WEB_APP_CLIENT_ID)
                .requestScopes(Scope(DriveScopes.DRIVE_APPDATA))
                .build()

            return GoogleSignIn.getClient(context, signInOptions)
        }

        override suspend fun signInWithFirebase(intent: Intent) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(intent).await()
            val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
            Firebase.auth.signInWithCredential(credentials).await()
        }

        override suspend fun signOut(context: Context) {
            Firebase.auth.signOut()
            GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut().await()
        }

        override fun profile(context: Context): ProfileUi? = Firebase.auth.currentUser?.map()

        private fun FirebaseUser.map(): ProfileUi = ProfileUi(
            email = email ?: "",
            name = displayName ?: "",
            imageUri = photoUrl,
            id = uid
        )
    }
}