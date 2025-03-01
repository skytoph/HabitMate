package com.github.skytoph.taski.presentation.settings.restore

import android.content.Context
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.MapResultToEvent
import com.github.skytoph.taski.presentation.core.state.StringResource

sealed interface RestoreResultUi : MapResultToEvent<RestoreEvent> {

    abstract class Success<T>(protected val data: T) : RestoreResultUi {
        class ListOfData(items: List<BackupItemUi>) : Success<List<BackupItemUi>>(items) {
            override fun apply(): RestoreEvent = RestoreEvent.UpdateList(data)
        }

        class NextAction(
            data: ByteArray,
            private val is24HoursFormat: Boolean,
            private val restoreSettings: Boolean,
            private val context: Context
        ) : Success<ByteArray>(data) {
            override fun apply(): RestoreEvent = RestoreEvent.Restore(data, restoreSettings, is24HoursFormat, context)
        }

        class Restored(
            private val containsReminders: Boolean,
            private val needsPermission: Boolean,
            message: SnackbarMessage
        ) : Success<SnackbarMessage>(message) {
            override fun apply(): RestoreEvent = when {
                containsReminders && needsPermission -> RestoreEvent.PermissionNeeded
                containsReminders -> RestoreEvent.RefreshingReminders(true)
                else -> RestoreEvent.Message(data)
            }
        }

        class AllBackupsDeleted(message: SnackbarMessage) : Success<SnackbarMessage>(message) {
            override fun apply(): RestoreEvent = RestoreEvent.BackupsDeleted(data)
        }

        class Deleted(private val message: SnackbarMessage, private val time: Long?, newData: List<BackupItemUi>) :
            Success<List<BackupItemUi>>(newData) {
            override fun apply(): RestoreEvent =
                RestoreEvent.BackupDeleted(message = message, lastModifierTime = time, newData = data)
        }
    }

    class Message(private val message: SnackbarMessage) : RestoreResultUi {
        override fun apply(): RestoreEvent = RestoreEvent.Message(message)
    }

    class ErrorState(private val message: StringResource) : RestoreResultUi {
        override fun apply(): RestoreEvent = RestoreEvent.ErrorFullscreen(message)
    }

    data object Empty : RestoreResultUi {
        override fun apply(): RestoreEvent = RestoreEvent.Empty
    }
}