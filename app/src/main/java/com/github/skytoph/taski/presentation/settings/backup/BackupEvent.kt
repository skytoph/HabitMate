package com.github.skytoph.taski.presentation.settings.backup

import android.net.Uri
import androidx.compose.runtime.MutableState

sealed interface BackupEvent {
    fun handle(state: MutableState<BackupState>)

    class ImportLoading(private val isLoading: Boolean) : BackupEvent {
        override fun handle(state: MutableState<BackupState>) {
            state.value = state.value.copy(importLoading = isLoading)
        }
    }

    class ExportLoading(private val isLoading: Boolean) : BackupEvent {
        override fun handle(state: MutableState<BackupState>) {
            state.value = state.value.copy(exportLoading = isLoading)
        }
    }

    class ShareFile(private val uri: Uri? = null) : BackupEvent {
        override fun handle(state: MutableState<BackupState>) {
            state.value = state.value.copy(uri = uri, exportLoading = false)
        }
    }
}