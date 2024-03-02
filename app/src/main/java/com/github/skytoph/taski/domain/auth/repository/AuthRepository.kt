package com.github.skytoph.taski.domain.auth.repository

import com.github.skytoph.taski.presentation.auth.authentication.AuthResult
import com.github.skytoph.taski.presentation.auth.authentication.user.UserData

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AuthResult?
    suspend fun signUp(email: String, password: String): AuthResult?
    suspend fun reloadUser(onVerified: (Boolean) -> Unit)
    suspend fun sendVerificationEmail()
    suspend fun signOut()
    fun currentUser(): UserData?
}