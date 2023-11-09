package com.github.skytoph.taski.presentation.auth.authentication.client

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.auth.authentication.AuthResult
import com.github.skytoph.taski.presentation.auth.authentication.user.UserData
import com.github.skytoph.taski.presentation.auth.authentication.error.AuthErrorHandler
import com.github.skytoph.taski.presentation.auth.authentication.mapper.AuthErrorMapper
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val oneTapClient: SignInClient,
    private val auth: FirebaseAuth = Firebase.auth,
    private val errorHandler: AuthErrorHandler = AuthErrorHandler(AuthErrorMapper())
) {

    suspend fun signIn(email: String, password: String): AuthResult = try {
        auth.signInWithEmailAndPassword(email, password).await().user?.run {
            AuthResult(user = this.toUserData())
        } ?: errorHandler.handle(null)
    } catch (exception: Exception) {
        errorHandler.handle(exception)
    }

    suspend fun signInWithGoogle(): IntentSender? = try {
        oneTapClient.beginSignIn(beginSignInRequest()).await()
    } catch (exception: Exception) {
        if (exception is CancellationException) throw exception
        Log.e(this::class.java.simpleName, exception.stackTraceToString())
        null
    }?.pendingIntent?.intentSender

    suspend fun signUp(email: String, password: String): AuthResult = try {
        auth.createUserWithEmailAndPassword(email, password).await().user?.run {
            AuthResult(user = toUserData())
        } ?: errorHandler.handle(null)
    } catch (exception: Exception) {
        errorHandler.handle(exception)
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            Log.e(this::class.java.simpleName, exception.stackTraceToString())
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.toUserData()

    suspend fun signInWithIntent(intent: Intent): AuthResult {
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

    private fun FirebaseUser.toUserData(): UserData = UserData(
        userId = uid,
        userName = displayName,
        email = email,
        profilePictureUrl = photoUrl?.toString(),
        isVerified = isEmailVerified
    )
}