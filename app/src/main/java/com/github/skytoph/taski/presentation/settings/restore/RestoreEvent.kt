package com.github.skytoph.taski.presentation.settings.restore

import androidx.compose.runtime.MutableState
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.PostMessage
import com.github.skytoph.taski.presentation.core.state.StringResource
import com.github.skytoph.taski.presentation.settings.SettingsViewModel

sealed interface RestoreEvent {
    fun handle(
        state: MutableState<RestoreState>,
        postMessage: PostMessage,
        restore: RestoreData,
        updateSettings: (SettingsEvent) -> Unit
    ) = Unit

    class UpdateList(private val items: List<BackupItemUi>) : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(items = items, isLoading = false)
        }
    }

    class ShowContextMenu(private val item: BackupItemUi? = null) : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(contextMenuItem = item)
        }
    }

    class UpdateDialog(private val dialog: RestoreDialogUi? = null) : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(dialog = dialog)
        }
    }

    class ErrorFullscreen(private val message: StringResource) : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(errorStateMessage = message, isLoading = false)
        }
    }

    class Message(private val message: SnackbarMessage) : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(isLoading = false)
            postMessage.postMessage(message)
        }
    }

    class Restore(private val data: ByteArray) : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            restore.restore(data)
        }
    }

    class Loading(private val loading: Boolean) : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(isLoading = loading)
        }
    }

    class BackupsDeleted(private val message: SnackbarMessage) : RestoreEvent, SettingsEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(isLoading = false, items = emptyList())
            postMessage.postMessage(message)
            updateSettings(this)
        }

        override suspend fun handle(settings: SettingsCache) {
            settings.updateBackupTime(null)
        }
    }

    class BackupDeleted(
        private val message: SnackbarMessage,
        private val lastModifierTime: Long?,
        private val newData: List<BackupItemUi>
    ) : RestoreEvent,
        SettingsEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(isLoading = false, items = newData)
            postMessage.postMessage(message)
            updateSettings(this)
        }

        override suspend fun handle(settings: SettingsCache) {
            settings.updateBackupTime(lastModifierTime)
        }
    }

    data object Empty : RestoreEvent

    fun interface RestoreData {
        fun restore(data: ByteArray)
    }

    interface SettingsEvent : SettingsViewModel.Event
}
