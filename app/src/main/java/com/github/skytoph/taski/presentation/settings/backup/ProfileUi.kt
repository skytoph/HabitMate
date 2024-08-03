package com.github.skytoph.taski.presentation.settings.backup

import android.net.Uri

data class ProfileUi(val email: String, val name: String, val imageUri: Uri? = null, val id: String = "")