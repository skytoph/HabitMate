package com.github.skytoph.taski.data.auth.repository

import com.github.skytoph.taski.domain.auth.repository.AuthRepository
import com.github.skytoph.taski.presentation.auth.authentication.AuthResult
import com.github.skytoph.taski.presentation.auth.authentication.client.toUserData
import com.github.skytoph.taski.presentation.auth.authentication.user.UserData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class BaseAuthRepository(private val auth: FirebaseAuth) : AuthRepository {

    init {
        auth.currentUser?.let { user ->
            user.getIdToken(true).addOnSuccessListener { user.reload() }
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult? =
        auth.signInWithEmailAndPassword(email, password).await().user?.run {
            AuthResult(user = this.toUserData())
        }

    override suspend fun signUp(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password).await().user?.run {
            AuthResult(user = toUserData())
        }

    override suspend fun reloadUser() {
        auth.currentUser?.let { user ->
            user.getIdToken(true)
            user.reload()
        }
    }

    override suspend fun sendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override fun currentUser(): UserData? = auth.currentUser?.toUserData()
}