package com.skytoph.taski.core.auth

import android.content.Context
import android.content.Intent
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.api.services.drive.DriveScopes
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.skytoph.taski.BuildConfig
import com.skytoph.taski.presentation.core.Logger
import com.skytoph.taski.presentation.settings.backup.ProfileUi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


interface SignInWithGoogle {
    fun profile(isBackupAvailable: Boolean = false): ProfileUi
    fun profileFlow(isBackupAvailable: Boolean): Flow<ProfileUi>
    suspend fun signIn(context: Context): GoogleIdTokenCredential?
    suspend fun signInWithFirebase(intent: Intent): AuthCredential?
    suspend fun signInWithCredentials(credentials: AuthCredential)
    suspend fun signInWithFirebase(credential: GoogleIdTokenCredential): AuthCredential?
    suspend fun signOut(context: Context)
    suspend fun deleteAccount(context: Context)
    suspend fun signInAnonymously()
    suspend fun backupAvailable(context: Context): Boolean
    suspend fun authorizeGoogleDrive(context: Context): AuthorizationResult
    fun authorizeGoogleDriveResult(context: Context, intent: Intent): AuthorizationResult

    class Base(private val log: Logger) : SignInWithGoogle {

        init {
            Firebase.appCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance())
        }

        private val profile: Flow<ProfileUi> = callbackFlow {
            val firebaseAuth = Firebase.auth
            val authStateListener = FirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser.map(false))
            }
            firebaseAuth.addAuthStateListener(authStateListener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(authStateListener)
            }
        }

        private val authorizationRequest =
            AuthorizationRequest.builder().setRequestedScopes(listOf(Scope(DriveScopes.DRIVE_APPDATA))).build()

        private val signInWithGoogleOption: GetSignInWithGoogleOption =
            GetSignInWithGoogleOption.Builder(BuildConfig.WEB_APP_CLIENT_ID).build()

        override suspend fun signIn(context: Context): GoogleIdTokenCredential? {
            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()
            return try {
                val credentialManager = CredentialManager.create(context)
                val result = credentialManager.getCredential(request = request, context = context)
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                log.log(e)
                null
            }
        }

        private fun handleSignIn(result: GetCredentialResponse): GoogleIdTokenCredential? {
            val credential = result.credential
            return if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    GoogleIdTokenCredential.createFrom(credential.data)
                } catch (e: GoogleIdTokenParsingException) {
                    log.log(e)
                    null
                }
            } else null
        }

        override suspend fun signInWithFirebase(credential: GoogleIdTokenCredential): AuthCredential? {
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

        override suspend fun authorizeGoogleDrive(context: Context): AuthorizationResult =
            Identity.getAuthorizationClient(context).authorize(authorizationRequest).await()

        override fun authorizeGoogleDriveResult(context: Context, intent: Intent): AuthorizationResult =
            Identity.getAuthorizationClient(context).getAuthorizationResultFromIntent(intent)

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
            val credentialManager = CredentialManager.create(context)
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }

        override suspend fun deleteAccount(context: Context) {
            Firebase.auth.currentUser?.delete()?.await()
            GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).revokeAccess().await()
        }

        override fun profile(isBackupAvailable: Boolean): ProfileUi =
            Firebase.auth.currentUser.map(isBackupAvailable)

        override fun profileFlow(isBackupAvailable: Boolean): Flow<ProfileUi> = profile

        override suspend fun backupAvailable(context: Context): Boolean =
            authorizeGoogleDrive(context).grantedScopes.contains(DriveScopes.DRIVE_APPDATA)

        private fun FirebaseUser?.map(isBackupAvailable: Boolean): ProfileUi = this?.run {
            ProfileUi(
                email = email ?: "",
                name = displayName ?: "",
                imageUri = photoUrl,
                id = uid,
                isAnonymous = isAnonymous,
                isBackupAvailable = isBackupAvailable
            )
        } ?: ProfileUi(isEmpty = true, isAnonymous = true)
    }
}