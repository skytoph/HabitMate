package com.github.skytoph.taski.presentation.auth.authentication

data class UserData(
    val userId: String,
    val userName: String?,
    val email: String?,
    val profilePictureUrl: String?,
    val isVerified: Boolean?,
)