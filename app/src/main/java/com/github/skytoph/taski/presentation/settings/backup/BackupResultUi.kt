package com.github.skytoph.taski.presentation.settings.backup

import android.net.Uri
import com.github.skytoph.taski.presentation.core.MapResultToListOfEvents
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.deletingAccountFailedMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.deletingAccountSucceededMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.exportFailedMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.iconsSynchronizeErrorMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.iconsSynchronizeSuccessMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.importFailedMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.importSucceededMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.signInFailedMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.signOutFailedMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.syncFailedMessage
import com.google.api.client.util.DateTime

sealed interface BackupResultUi : MapResultToListOfEvents<BackupEvent> {

    abstract class Success<T>(protected val data: T) : BackupResultUi {

        class BackupSaved(time: Long) : Success<Long>(time) {
            override fun apply(): List<BackupEvent> = listOf(BackupEvent.UpdateLastBackup(data))
        }

        class ProfileLoaded(profile: ProfileUi) : Success<ProfileUi>(profile) {
            override fun apply(): List<BackupEvent> = listOf(BackupEvent.UpdateProfile(data))
        }

        class SignedIn(profile: ProfileUi, private val sync: DateTime?, private val synchronized: Boolean?) :
            Success<ProfileUi>(profile) {
            override fun apply(): List<BackupEvent> = listOf(
                BackupEvent.UpdateLastBackup(sync?.value),
                BackupEvent.IsSigningIn(false),
                BackupEvent.UpdateProfile(data),
            ).let { list ->
                if (synchronized == null) list
                else list + BackupEvent.Message(if (synchronized) iconsSynchronizeSuccessMessage else iconsSynchronizeErrorMessage)
            }
        }

        class BackupExported(uri: Uri) : Success<Uri>(uri) {
            override fun apply(): List<BackupEvent> = listOf(BackupEvent.ShareFile(data))
        }
    }

    class Imported(
        private val successful: Boolean,
        private val containsReminders: Boolean = false,
        private val needsPermission: Boolean = false
    ) : BackupResultUi {
        override fun apply(): List<BackupEvent> =
            listOf(BackupEvent.ImportLoading(false)) + when {
                !successful -> listOf(BackupEvent.Message(importFailedMessage))
                containsReminders && needsPermission -> listOf(BackupEvent.PermissionNeeded)
                containsReminders ->
                    listOf(BackupEvent.RefreshingReminders(true), BackupEvent.Message(importSucceededMessage))

                else -> listOf(BackupEvent.Message(importSucceededMessage))
            }

    }

    data object ExportFailed : BackupResultUi {
        override fun apply(): List<BackupEvent> = listOf(
            BackupEvent.ExportLoading(false), BackupEvent.Message(exportFailedMessage)
        )
    }

    data object BackupFailed : BackupResultUi {
        override fun apply(): List<BackupEvent> = listOf(
            BackupEvent.DriveLoading(false), BackupEvent.Message(syncFailedMessage)
        )
    }

    data object NoConnection : BackupResultUi {
        override fun apply(): List<BackupEvent> = listOf(BackupEvent.UpdateConnection(false))
    }

    data object ProfileLoadingFailed : BackupResultUi {
        override fun apply(): List<BackupEvent> = listOf(BackupEvent.UpdateProfile(null))
    }

    data object SignInFailed : BackupResultUi {
        override fun apply(): List<BackupEvent> = listOf(
            BackupEvent.IsSigningIn(false),
            BackupEvent.UpdateProfile(null),
            BackupEvent.Message(signInFailedMessage)
        )
    }

    data object SignOutFailed : BackupResultUi {
        override fun apply(): List<BackupEvent> = listOf(
            BackupEvent.UpdateProfile(null),
            BackupEvent.Message(signOutFailedMessage)
        )
    }

    data class DeletingAccount(private val deleted: Boolean) : BackupResultUi {
        override fun apply(): List<BackupEvent> = if (deleted) listOf(
            BackupEvent.UpdateProfile(),
            BackupEvent.UpdateLastBackup(null),
            BackupEvent.Message(deletingAccountSucceededMessage)
        ) else listOf(
            BackupEvent.UpdateProfile(isLoading = false),
            BackupEvent.Message(deletingAccountFailedMessage)
        )
    }

    data object Empty : BackupResultUi {
        override fun apply(): List<BackupEvent> = listOf(BackupEvent.Empty)
    }
}