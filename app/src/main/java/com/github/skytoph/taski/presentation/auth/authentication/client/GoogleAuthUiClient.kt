package com.github.skytoph.taski.presentation.auth.authentication.client

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.github.skytoph.taski.BuildConfig
import com.github.skytoph.taski.presentation.auth.authentication.AuthResult
import com.github.skytoph.taski.presentation.auth.authentication.error.AuthErrorHandler
import com.github.skytoph.taski.presentation.auth.authentication.user.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

interface GoogleAuth {
    suspend fun signInWithGoogle(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): AuthResult
    fun getSignedInUser(): UserData?
}

class GoogleAuthUiClient(
    private val oneTapClient: SignInClient,
    private val auth: FirebaseAuth,
    private val errorHandler: AuthErrorHandler
) : GoogleAuth {

    override fun getSignedInUser(): UserData? = auth.currentUser?.toUserData()

    override suspend fun signInWithGoogle(): IntentSender? = try {
        oneTapClient.beginSignIn(beginSignInRequest()).await()
    } catch (exception: Exception) {
        if (exception is CancellationException) throw exception
        Log.e(this::class.java.simpleName, exception.stackTraceToString())
        null
    }?.pendingIntent?.intentSender

    override suspend fun signInWithIntent(intent: Intent): AuthResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            auth.signInWithCredential(googleCredentials).await().user?.run {
                AuthResult(user = this.toUserData())
            } ?: errorHandler.handle(null)
        } catch (exception: Exception) {
            errorHandler.handle(exception)
        }
    }

    private fun beginSignInRequest(): BeginSignInRequest =
        BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
}

fun FirebaseUser.toUserData(): UserData = UserData(
    userId = uid,
    userName = displayName,
    email = email,
    profilePictureUrl = photoUrl?.toString(),
    isVerified = isEmailVerified
)