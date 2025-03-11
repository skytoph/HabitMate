package com.skytoph.taski.presentation.settings.restore

import android.content.Context
import androidx.compose.runtime.MutableState
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.core.PostMessage
import com.skytoph.taski.presentation.core.state.StringResource
import com.skytoph.taski.presentation.habit.list.component.DialogItem
import com.skytoph.taski.presentation.settings.SettingsViewModel

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
            state.value = state.value.copy(
                dialog = dialog, restoreSettings = if (dialog == null) false else state.value.restoreSettings
            )
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

    class Restore(
        private val data: ByteArray,
        private val restoreSettings: Boolean,
        private val is24HoursFormat: Boolean,
        private val context: Context
    ) : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            restore.restore(data, restoreSettings, is24HoursFormat, context)
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

    data object PermissionNeeded : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(isLoading = false, dialog = RestoreDialogUi.RequestPermissions)
        }
    }

    class RefreshingReminders(private val isRefreshing: Boolean) : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(isLoading = false, refreshingReminders = isRefreshing)
        }
    }

    class RequestPermissions(private val request: Boolean) : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(dialog = null, requestingPermission = request)
        }
    }

    data object UpdateRestoreSettings : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(restoreSettings = !state.value.restoreSettings)
        }
    }

    class UpdatePermissionDialog(private val dialog: DialogItem? = null) : RestoreEvent {
        override fun handle(
            state: MutableState<RestoreState>,
            postMessage: PostMessage,
            restore: RestoreData,
            updateSettings: (SettingsEvent) -> Unit
        ) {
            state.value = state.value.copy(
                permissionDialog = dialog,
                restoreSettings = if (dialog == null) false else state.value.restoreSettings
            )
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
        fun restore(data: ByteArray, restoreSettings: Boolean, is24HoursFormat: Boolean, context: Context)
    }

    interface SettingsEvent : SettingsViewModel.Event
}
