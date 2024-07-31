package com.github.skytoph.taski.presentation.settings.backup

import android.net.Uri
import androidx.compose.runtime.MutableState
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.PostMessage
import com.github.skytoph.taski.presentation.settings.SettingsViewModel

sealed interface BackupEvent {
    fun handle(state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit)

    class ImportLoading(private val isLoading: Boolean) : BackupEvent {
        override fun handle(
            state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(isImportLoading = isLoading)
        }
    }

    class DriveLoading(private val isLoading: Boolean) : BackupEvent {
        override fun handle(
            state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(isDriveBackupLoading = isLoading)
        }
    }

    class ExportLoading(private val isLoading: Boolean) : BackupEvent {
        override fun handle(
            state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(isExportLoading = isLoading)
        }
    }

    class ShareFile(private val uri: Uri? = null) : BackupEvent {
        override fun handle(
            state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(uriShareFile = uri, isExportLoading = false)
        }
    }

    class UpdateProfile(private val profile: ProfileUi? = null, private val isLoading: Boolean? = null) : BackupEvent {
        override fun handle(
            state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit
        ) {
            state.value = if (isLoading != null) state.value.copy(isProfileLoading = isLoading)
            else state.value.copy(profile = profile, isProfileLoading = false)
        }
    }

    class IsSigningIn(private val isSigningIn: Boolean) : BackupEvent {
        override fun handle(
            state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(isSigningInLoading = isSigningIn)
        }
    }

    class UpdateDialog(private val dialog: BackupDialogUi? = null) : BackupEvent {
        override fun handle(
            state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(dialog = dialog)
        }
    }

    class Message(private val message: SnackbarMessage) : BackupEvent {
        override fun handle(
            state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit
        ) {
            showMessage.postMessage(message)
        }
    }

    class UpdateBackupTime(private val lastBackupSaved: Long?) : BackupEvent {
        override fun handle(
            state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(lastBackupSavedTime = lastBackupSaved)
        }
    }

    class UpdateLastBackup(private val time: Long? = null) : BackupEvent, SettingsEvent {

        override fun handle(
            state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(isDriveBackupLoading = false)
            settingsEvent(this)
        }

        override suspend fun handle(settings: SettingsCache) {
            settings.updateBackupTime(time)
        }
    }

    data object Empty : BackupEvent {
        override fun handle(
            state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit
        ) = Unit
    }

    class UpdateConnection(private val isConnected: Boolean = true) : BackupEvent {
        override fun handle(
            state: MutableState<BackupState>, showMessage: PostMessage, settingsEvent: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(isInternetConnected = isConnected)
        }
    }

    interface SettingsEvent : SettingsViewModel.Event
}