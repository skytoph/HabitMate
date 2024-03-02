package com.github.skytoph.taski.data.auth.repository

import com.github.skytoph.taski.domain.auth.repository.AuthRepository
import com.github.skytoph.taski.presentation.auth.authentication.AuthResult
import com.github.skytoph.taski.presentation.auth.authentication.client.toUserData
import com.github.skytoph.taski.presentation.auth.authentication.error.AuthErrorHandler
import com.github.skytoph.taski.presentation.auth.authentication.user.UserData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class BaseAuthRepository(
    private val auth: FirebaseAuth,
    private val errorHandler: AuthErrorHandler
) : AuthRepository {

    init {
        auth.currentUser?.let { user ->
            user.getIdToken(true).addOnSuccessListener { auth.currentUser?.reload() }
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult? = try {
        auth.signInWithEmailAndPassword(email, password).await().user?.let {
            AuthResult(user = it.toUserData())
        }
    } catch (exception: Exception) {
        errorHandler.handle(exception)
    }

    override suspend fun signUp(email: String, password: String) = try {
        auth.createUserWithEmailAndPassword(email, password).await().user?.let {
            AuthResult(user = it.toUserData())
        }
    } catch (exception: Exception) {
        errorHandler.handle(exception)
    }

    override suspend fun reloadUser(onVerified: (Boolean) -> Unit) {
        auth.currentUser?.getIdToken(true)?.await()
        auth.currentUser?.reload()?.await()
        onVerified(auth.currentUser?.isEmailVerified == true)
    }

    override suspend fun sendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override fun currentUser(): UserData? = auth.currentUser?.toUserData()
}