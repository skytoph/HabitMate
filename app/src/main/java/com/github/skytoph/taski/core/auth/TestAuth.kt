package com.github.skytoph.taski.core.auth

import android.content.Context
import android.content.Intent
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.github.skytoph.taski.BuildConfig.WEB_APP_CLIENT_ID
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.api.services.drive.DriveScopes
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object TestAuth {

    private val authorizationRequest =
        AuthorizationRequest.builder().setRequestedScopes(listOf(Scope(DriveScopes.DRIVE_APPDATA))).build()

    private val signInWithGoogleOption: GetSignInWithGoogleOption =
        GetSignInWithGoogleOption.Builder(WEB_APP_CLIENT_ID).build()

    suspend fun signIn(context: Context): GoogleIdTokenCredential? {
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()
        return try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(request = request, context = context)
            handleSignIn(result)
        } catch (e: GetCredentialException) {
//            handleFailure(e)
            null
        }
    }

    private fun handleSignIn(result: GetCredentialResponse): GoogleIdTokenCredential? {
        val credential = result.credential
        return if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                GoogleIdTokenCredential.createFrom(credential.data)
            } catch (e: GoogleIdTokenParsingException) {
                null
            }
        } else null
    }

    suspend fun delete(context: Context) {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }

    suspend fun signInWithFirebase(credential: GoogleIdTokenCredential): AuthCredential? {
        val credentials = GoogleAuthProvider.getCredential(credential.idToken, null)
        val currentUser = Firebase.auth.currentUser
        return if (currentUser == null) credentials
        else try {
            currentUser.linkWithCredential(credentials).await()
            val request = UserProfileChangeRequest.Builder()
                .setPhotoUri(credential.profilePictureUri)
                .setDisplayName(credential.displayName)
                .build()
            Firebase.auth.currentUser?.updateProfile(request)?.await()
            null
        } catch (_: FirebaseAuthUserCollisionException) {
            credentials
        }
    }

    suspend fun authorizeGoogleDrive(context: Context): AuthorizationResult =
        Identity.getAuthorizationClient(context).authorize(authorizationRequest).await()

    fun authorizeGoogleDriveResult(context: Context, intent: Intent): AuthorizationResult =
        Identity.getAuthorizationClient(context).getAuthorizationResultFromIntent(intent)
}