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
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface SignInWithGoogle<C> {
    fun getClient(context: Context): C
    fun profile(): ProfileUi?
    suspend fun signInWithFirebase(intent: Intent): AuthCredential?
    suspend fun signInWithCredentials(credentials: AuthCredential)
    suspend fun signOut(context: Context)
    suspend fun deleteAccount(context: Context)
    suspend fun signInAnonymously()

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

        override suspend fun signInAnonymously() {
            if (Firebase.auth.currentUser == null) Firebase.auth.signInAnonymously().await()
        }

        override suspend fun signInWithFirebase(intent: Intent): AuthCredential? {
            val account = GoogleSignIn.getSignedInAccountFromIntent(intent).await()
            val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
            val currentUser = Firebase.auth.currentUser
            return if (currentUser == null) credentials
            else try {
                currentUser.linkWithCredential(credentials).await()
                val request = UserProfileChangeRequest.Builder()
                    .setPhotoUri(account.photoUrl)
                    .setDisplayName(account.displayName)
                    .build()
                Firebase.auth.currentUser?.updateProfile(request)?.await()
                null
            } catch (_: FirebaseAuthUserCollisionException) {
                credentials
            }
        }

        override suspend fun signInWithCredentials(credentials: AuthCredential) {
            Firebase.auth.currentUser.let { if (it?.isAnonymous == true) it.delete().await() }
            Firebase.auth.signInWithCredential(credentials).await()
        }

        override suspend fun signOut(context: Context) {
            Firebase.auth.signOut()
            GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut().await()
        }

        override suspend fun deleteAccount(context: Context) {
            Firebase.auth.currentUser?.delete()?.await()
            GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).revokeAccess().await()
        }

        override fun profile(): ProfileUi = Firebase.auth.currentUser.map()

        private fun FirebaseUser?.map(): ProfileUi = this?.run {
            ProfileUi(
                email = email ?: "",
                name = displayName ?: "",
                imageUri = photoUrl,
                id = uid,
                isAnonymous = isAnonymous,
            )
        } ?: ProfileUi(isEmpty = true, isAnonymous = true)
    }
}