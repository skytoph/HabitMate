package com.github.skytoph.taski.data.auth.client

import android.util.Log
import com.github.skytoph.taski.presentation.auth.authentication.AuthResult
import com.github.skytoph.taski.presentation.auth.authentication.error.AuthErrorHandler
import com.github.skytoph.taski.presentation.auth.authentication.user.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

// TODO: delete
//class FirebaseAuthClient(
//    private val auth: FirebaseAuth = Firebase.auth,
//    private val errorHandler: AuthErrorHandler
//) {
//    init {
//        initUser()
//    }
//
//    private fun initUser() = auth.currentUser?.let { user ->
//        user.getIdToken(true).addOnSuccessListener { user.reload() }
//    }
//
//    suspend fun signIn(email: String, password: String): AuthResult = try {
//        auth.signInWithEmailAndPassword(email, password).await().user?.run {
//            AuthResult(user = this.toUserData())
//        } ?: errorHandler.handle(null)
//    } catch (exception: Exception) {
//        errorHandler.handle(exception)
//    }
//
//    suspend fun signUp(email: String, password: String): AuthResult = try {
//        auth.createUserWithEmailAndPassword(email, password).await().user?.run {
//            AuthResult(user = toUserData())
//        } ?: errorHandler.handle(null)
//    } catch (exception: Exception) {
//        errorHandler.handle(exception)
//    }
//
//    suspend fun signOut() {
//        try {
//            auth.signOut()
//        } catch (exception: Exception) {
//            if (exception is CancellationException) throw exception
//            Log.e(this::class.java.simpleName, exception.stackTraceToString())
//        }
//    }
//
//    fun getSignedInUser(): UserData? = auth.currentUser?.toUserData()
//
//    suspend fun reloadUser() {
//        auth.currentUser?.getIdToken(true)?.await()
//        auth.currentUser?.reload()?.await()
//    }
//
//    fun sendEmailVerification() = try {
//        auth.currentUser?.sendEmailVerification()
//    } catch (exception: Exception) {
//        errorHandler.handle(exception)
//    }
//
//    // TODO: DRY
//    private fun FirebaseUser.toUserData(): UserData = UserData(
//        userId = uid,
//        userName = displayName,
//        email = email,
//        profilePictureUrl = photoUrl?.toString(),
//        isVerified = isEmailVerified
//    )
//}