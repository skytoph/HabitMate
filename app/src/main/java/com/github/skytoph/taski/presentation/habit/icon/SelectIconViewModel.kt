package com.github.skytoph.taski.presentation.habit.icon

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.appbar.PopupMessage
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.settings.SettingsViewModel
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectIconViewModel @Inject constructor(
    private val iconState: MutableState<IconState>,
    private val state: MutableState<SelectIconState>,
    private val interactor: IconsInteractor,
    private val snackbar: PopupMessage.Show<SnackbarMessage>,
    settings: SettingsCache,
    initAppBar: InitAppBar
) : SettingsViewModel<SelectIconEvent.SettingsEvent>(settings, initAppBar) {

    init {
        viewModelScope.launch {
            onEvent(SelectIconEvent.IsWarningShown(settings().first().showIconWarning && interactor.shouldShowWarning()))
        }
    }

    fun init(resources: Resources) {
        interactor.icons(resources)
            .flowOn(Dispatchers.IO)
            .combine(settings()) { groups, settings -> if (settings.sortIcons) sort(groups) else groups }
            .onEach { icons -> onEvent(SelectIconEvent.Initialize(icons)) }
            .launchIn(viewModelScope)
    }

    private fun sort(groups: List<IconsLockedGroup>) =
        groups.map { group -> group.copy(icons = group.icons.sortedBy { icon -> !icon.second }) }

    fun onEvent(event: SelectIconEvent) = event.handle(iconState, state)

    fun iconState(): State<IconState> = iconState

    fun state(): State<SelectIconState> = state

    fun unlockIcon(icon: String) {
        onEvent(SelectIconEvent.UpdateDialog()) // todo implement
        viewModelScope.launch(Dispatchers.IO) {
            interactor.unlock(icon)
        }
    }

    fun signInWithFirebase(intent: Intent, context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val success = interactor.signInWithFirebase(intent, context)
        withContext(Dispatchers.Main) {
            onEvent(SelectIconEvent.IsSigningIn(false))
            onEvent(SelectIconEvent.IsWarningShown(!success))
            if (!success) showMessage(BackupMessages.signInFailedMessage)
        }
    }

    fun hideWarning() {
        onEvent(SelectIconEvent.DoNotShowWarning)
        onEvent(SelectIconEvent.IsWarningDialogShown(false))
        onEvent(SelectIconEvent.IsWarningShown(false))
    }

    fun showMessage(message: SnackbarMessage) = viewModelScope.launch { snackbar.show(message) }

    fun connected(context: Context): Boolean = interactor.checkConnection(context)
}