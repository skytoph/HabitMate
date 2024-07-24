package com.github.skytoph.taski.presentation.settings.backup

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.appbar.PopupMessage
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val state: MutableState<BackupState>,
    private val interactor: BackupInteractor,
    private val snackbar: PopupMessage.Show<SnackbarMessage>,
    initAppBar: InitAppBar
) : ViewModel(), InitAppBar by initAppBar {

    fun export(context: Context, errorMessage: SnackbarMessage) {
        onEvent(BackupEvent.ExportLoading(true))
        viewModelScope.launch(Dispatchers.IO) {
            val uri = interactor.export(context)
            withContext(Dispatchers.Main) {
                if (uri == null) {
                    snackbar.show(errorMessage)
                    onEvent(BackupEvent.ExportLoading(false))
                } else onEvent(BackupEvent.ShareFile(uri))
            }
        }
    }

    fun import(contentResolver: ContentResolver, uri: Uri, success: SnackbarMessage, error: SnackbarMessage) {
        onEvent(BackupEvent.ImportLoading(true))
        viewModelScope.launch(Dispatchers.IO) {
            val successful = interactor.import(contentResolver, uri)
            onEvent(BackupEvent.ImportLoading(false))
            snackbar.show(if (successful) success else error)
        }
    }

    fun onEvent(event: BackupEvent) = event.handle(state)

    fun state(): State<BackupState> = state
}