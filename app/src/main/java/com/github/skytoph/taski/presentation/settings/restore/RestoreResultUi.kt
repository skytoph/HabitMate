package com.github.skytoph.taski.presentation.settings.restore

import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.MapResultToEvent
import com.github.skytoph.taski.presentation.core.state.StringResource

sealed interface RestoreResultUi : MapResultToEvent<RestoreEvent> {

    abstract class Success<T>(protected val data: T) : RestoreResultUi {
        class ListOfData(items: List<BackupItemUi>) : Success<List<BackupItemUi>>(items) {
            override fun apply(): RestoreEvent = RestoreEvent.UpdateList(data)
        }

        class NextAction(data: ByteArray) : Success<ByteArray>(data) {
            override fun apply(): RestoreEvent = RestoreEvent.Restore(data)
        }

        class Message(message: SnackbarMessage) : Success<SnackbarMessage>(message) {
            override fun apply(): RestoreEvent = RestoreEvent.Message(data)
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

    object Empty: RestoreResultUi{
        override fun apply(): RestoreEvent = RestoreEvent.Empty
    }
}