package com.skytoph.taski.presentation.settings.restore

import android.content.Context
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
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RestoreViewModel @Inject constructor(
    private val state: MutableState<RestoreState>,
    private val interactor: RestoreInteractor,
    private val snackbar: PopupMessage.Show<SnackbarMessage>,
    settings: SettingsCache,
    initAppBar: InitAppBar
) : SettingsViewModel<RestoreEvent.SettingsEvent>(settings, initAppBar) {

    private val actionHandler: ViewModelAction<RestoreResultUi, RestoreEvent> =
        ViewModelAction.Base(scope = viewModelScope, eventHandler = ::onEvent, mapper = { listOf(it.apply()) })

    fun state(): State<RestoreState> = state

    fun onEvent(event: RestoreEvent) =
        event.handle(state = state, postMessage = ::showMessage, restore = ::restoreBackup, updateSettings = ::onEvent)

    fun showMessage(message: SnackbarMessage) = viewModelScope.launch { snackbar.show(message) }

    fun loadItems(locale: Locale, is24HoursFormat: Boolean, context: Context) = actionHandler.action(
        doAction = { interactor.backupItems(locale, context, is24HoursFormat) },
    )

    fun downloadBackup(id: String, is24HoursFormat: Boolean, context: Context) =
        state.value.restoreSettings.let { restoreSettings ->
            actionHandler.action(
                beforeAction = arrayOf(RestoreEvent.UpdateDialog(), RestoreEvent.Loading(true)),
                doAction = { interactor.download(id, restoreSettings, context, is24HoursFormat) },
            )
        }

    private fun restoreBackup(data: ByteArray, restoreSettings: Boolean, is24HoursFormat: Boolean, context: Context) =
        actionHandler.action(
            beforeAction = emptyArray(),
            doAction = { interactor.restore(data, context, restoreSettings, is24HoursFormat) },
        )

    fun delete(id: String, locale: Locale, is24HoursFormat: Boolean, context: Context) = actionHandler.action(
        beforeAction = arrayOf(RestoreEvent.UpdateDialog(), RestoreEvent.Loading(true)),
        doAction = { interactor.delete(id, locale, context, is24HoursFormat) },
    )

    fun deleteAllData() = actionHandler.action(
        beforeAction = arrayOf(RestoreEvent.UpdateDialog(), RestoreEvent.Loading(true)),
        doAction = { interactor.deleteAllData() },
    )
}