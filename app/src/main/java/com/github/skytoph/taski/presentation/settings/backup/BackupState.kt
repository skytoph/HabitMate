package com.github.skytoph.taski.presentation.settings.backup

import android.net.Uri

data class BackupState(
    val isImportLoading: Boolean = false,
    val isExportLoading: Boolean = false,
    val isInternetConnected: Boolean = true,
    val isDriveBackupLoading: Boolean = false,
    val isProfileLoading: Boolean = true,
    val isSigningInLoading: Boolean = false,
    val uriShareFile: Uri? = null,
    val profile: ProfileUi? = null,
    val lastBackupSavedTime: Long? = null,
    val dialog: BackupDialogUi? = null,
)

enum class BackupDialogUi {
    Export,
    Import,
    SignOut,
    DeleteAccount,
}