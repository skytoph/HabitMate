package com.github.skytoph.taski.presentation.settings.backup

import android.net.Uri

data class BackupState(
    val importLoading: Boolean = false,
    val exportLoading: Boolean = false,
    val uri: Uri? = null,
)
