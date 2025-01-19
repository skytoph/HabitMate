package com.github.skytoph.taski.presentation.settings.backup

import android.net.Uri
import com.github.skytoph.taski.presentation.habit.list.component.DialogItem

data class BackupState(
    val isImportLoading: Boolean = false,
    val isExportLoading: Boolean = false,
    val isClearingLoading: Boolean = false,
    val isInternetConnected: Boolean = true,
    val isDriveBackupLoading: Boolean = false,
    val isProfileLoading: Boolean = true,
    val isSigningInLoading: Boolean = false,
    val restoreSettings: Boolean = false,
    val uriShareFile: Uri? = null,
    val profile: ProfileUi? = null,
    val lastBackupSavedTime: Long? = null,
    val dialog: BackupDialogUi? = null,
    val refreshingReminders: Boolean = false,
    val requestingPermission: Boolean = false,
    val permissionDialog: DialogItem? = null,
)

enum class BackupDialogUi {
    Export,
    Import,
    RequestPermissions,
    SignOut,
    DeleteAccount,
    Clear,
}