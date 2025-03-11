package com.skytoph.taski.presentation.settings.backup

import android.net.Uri

data class ProfileUi(
    val email: String = "",
    val name: String = "",
    val imageUri: Uri? = null,
    val id: String = "",
    val isAnonymous: Boolean = false,
    val isEmpty: Boolean = false,
    val isBackupAvailable: Boolean = false
)