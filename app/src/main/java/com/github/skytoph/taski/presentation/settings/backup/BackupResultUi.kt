package com.github.skytoph.taski.presentation.settings.backup

import android.net.Uri
import com.github.skytoph.taski.presentation.core.MapResultToListOfEvents
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.deletingAccountFailedMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.deletingAccountSucceededMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.exportFailedMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.importFailedMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.importSucceededMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.loadingAccountFailedMessage
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

        class SignedIn(profile: ProfileUi, private val sync: DateTime?) : Success<ProfileUi>(profile) {
            override fun apply(): List<BackupEvent> = listOf(
                BackupEvent.UpdateLastBackup(sync?.value),
                BackupEvent.IsSigningIn(false),
                BackupEvent.UpdateProfile(data)
            )
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
        override fun apply(): List<BackupEvent> = ArrayList<BackupEvent>(3).apply {
            add(BackupEvent.ImportLoading(false))
            when {
                !successful -> add(BackupEvent.Message(importFailedMessage))
                containsReminders && needsPermission -> add(BackupEvent.PermissionNeeded)
                containsReminders -> {
                    add(BackupEvent.RefreshingReminders(true))
                    add(BackupEvent.Message(importSucceededMessage))
                }

                else -> add(BackupEvent.Message(importSucceededMessage))
            }
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
            BackupEvent.Message(loadingAccountFailedMessage)
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