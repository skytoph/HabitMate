package com.skytoph.taski.presentation.settings.backup

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.presentation.appbar.InitAppBar
import com.skytoph.taski.presentation.appbar.PopupMessage
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.core.ViewModelAction
import com.skytoph.taski.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val state: MutableState<BackupState>,
    private val interactor: BackupInteractor,
    private val snackbar: PopupMessage.Show<SnackbarMessage>,
    settings: SettingsCache,
    initAppBar: InitAppBar
) : SettingsViewModel<BackupEvent.SettingsEvent>(settings, initAppBar), BackupEvent.BackupEventHandler {

    private val actionHandler: ViewModelAction<BackupResultUi, BackupEvent> =
        ViewModelAction.Base(scope = viewModelScope, eventHandler = ::onEvent, mapper = { it.apply() })

    init {
        viewModelScope.launch {
            settings().collect { settings ->
                onEvent(BackupEvent.UpdateBackupTime(settings.lastBackupSaved))
            }
        }
    }

    fun export(context: Context) = actionHandler.action(
        beforeAction = arrayOf(BackupEvent.ExportLoading(true), BackupEvent.UpdateDialog()),
        doAction = { interactor.export(context) })

    fun import(contentResolver: ContentResolver, uri: Uri, context: Context) = actionHandler.action(
        beforeAction = arrayOf(BackupEvent.UpdateDialog()),
        doAction = { interactor.import(contentResolver, uri, context, state.value.restoreSettings) })

    fun clearLocalData() = actionHandler.action(
        beforeAction = arrayOf(BackupEvent.UpdateDialog(), BackupEvent.IsClearingLoading(true)),
        doAction = { interactor.clear() })

    fun saveBackupOnDrive(context: Context) = actionHandler.action(
        beforeAction = arrayOf(BackupEvent.DriveLoading(true)),
        doAction = { interactor.saveBackupOnDrive(context) })

    fun loadProfile(context: Context) = actionHandler.action(
        beforeAction = emptyArray(),
        doAction = { interactor.profile(context) }
    )

    fun signOut(context: Context) = actionHandler.action(
        beforeAction = arrayOf(
            BackupEvent.UpdateDialog(), BackupEvent.UpdateProfile(isLoading = true), BackupEvent.UpdateLastBackup()
        ),
        doAction = { interactor.signOut(context) },
    )

    fun checkConnection(context: Context) = actionHandler.action(
        beforeAction = arrayOf(BackupEvent.UpdateDialog()),
        doAction = { interactor.checkConnection(context) }
    )

    fun deleteAccount(context: Context) = actionHandler.action(
        beforeAction = arrayOf(BackupEvent.UpdateDialog(), BackupEvent.UpdateProfile(isLoading = true)),
        doAction = { interactor.deleteAccount(context) }
    )

    fun onEvent(event: BackupEvent) = event.handle(state = state, handler = this)

    override fun showMessage(message: SnackbarMessage) {
        viewModelScope.launch { snackbar.show(message) }
    }

    fun state(): State<BackupState> = state

    fun mapBackupTime(time: Long?, context: Context, locale: Locale, is24HoursFormat: Boolean): String? =
        interactor.mapTime(time, TIME_IS_LOADING_VALUE, context, locale, is24HoursFormat)

    fun signIn(context: Context) = actionHandler.action(
        beforeAction = arrayOf(BackupEvent.IsSigningIn(true)),
        doAction = { interactor.signIn(context, true) })

    fun signInFailed(result: ActivityResult? = null) {
        onEvent(BackupEvent.IsSigningIn(false))
        interactor.log(result?.data)
    }

    override fun updateSettings(event: BackupEvent.SettingsEvent) {
        onEvent(event)
    }

    fun authorizeResult(context: Context, data: Intent?) = viewModelScope.launch(Dispatchers.IO) {
        interactor.authorizeResult(context, data)
        withContext(Dispatchers.Main) {
            onEvent(BackupEvent.IsSigningIn(false))
            loadProfile(context)
        }
    }

    fun backupPermissionRequest(context: Context, requestPermission: suspend (IntentSender) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            val request = interactor.backupPermissionRequest(context)
            withContext(Dispatchers.Main) {
                request?.let { requestPermission(request) }
                onEvent(BackupEvent.RequestBackupPermission(false))
            }
        }

    companion object {
        const val TIME_IS_LOADING_VALUE = -1L
    }
}