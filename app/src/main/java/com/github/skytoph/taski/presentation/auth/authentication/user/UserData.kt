package com.github.skytoph.taski.presentation.auth.authentication.user

data class UserData(
    val userId: String,
    val userName: String?,
    val email: String?,
    val profilePictureUrl: String?,
    val isVerified: Boolean?,
)