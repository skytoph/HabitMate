package com.github.skytoph.taski.presentation.habit.icon

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.core.NetworkManager
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.appbar.PopupMessage
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.settings.SettingsViewModel
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.iconsSynchronizeSuccessMessage
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
    private val rewards: RewardDataSource,
    private val networkManager: NetworkManager,
    settings: SettingsCache,
    initAppBar: InitAppBar
) : SettingsViewModel<SelectIconEvent.SettingsEvent>(settings, initAppBar) {

    fun init(activity: Activity) {
        viewModelScope.launch {
            val isWarningShown = settings().first().showIconWarning && interactor.shouldShowWarning(activity)
            withContext(Dispatchers.Main) { onEvent(SelectIconEvent.IsWarningShown(isWarningShown)) }
        }
        interactor.icons(activity)
            .flowOn(Dispatchers.IO)
            .combine(settings()) { groups, settings -> if (settings.sortIcons) sort(groups) else groups }
            .onEach { icons -> onEvent(SelectIconEvent.Initialize(icons)) }
            .launchIn(viewModelScope)
        rewards.load(activity)
    }

    private fun sort(groups: List<IconsLockedGroup>) =
        groups.map { group -> group.copy(icons = group.icons.sortedBy { icon -> !icon.second }) }

    fun onEvent(event: SelectIconEvent) = event.handle(iconState, state)

    fun iconState(): State<IconState> = iconState

    fun state(): State<SelectIconState> = state

    fun signIn(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val success = interactor.signIn(context, false)
        val syncTime = if (success) interactor.lastSync() else null
        withContext(Dispatchers.Main) {
            onEvent(SelectIconEvent.IsSigningIn(false))
            onEvent(SelectIconEvent.IsWarningShown(!success))
            if (!success) showMessage(BackupMessages.signInFailedMessage)
            else {
                showMessage(iconsSynchronizeSuccessMessage)
                onEvent(SelectIconEvent.UpdateLastSync(syncTime))
            }
        }
    }

    fun hideWarning(neverShowAgain: Boolean) {
        if (neverShowAgain) onEvent(SelectIconEvent.DoNotShowWarning)
        onEvent(SelectIconEvent.IsWarningDialogShown(false))
        onEvent(SelectIconEvent.IsWarningShown(false))
    }

    fun showMessage(message: SnackbarMessage) = viewModelScope.launch { snackbar.show(message) }

    fun connected(context: Context): Boolean = interactor.checkConnection(context)

    fun unlockIcon(icon: IconResource, activity: Activity) {
        if (networkManager.isNetworkAvailable()) {
            onEvent(SelectIconEvent.UpdateDialog(isLoading = true))
            rewards.show(activity = activity, reward = { reward(icon, activity) }, fail = { rewardFailed() })
        } else {
            onEvent(SelectIconEvent.UpdateDialog())
            showMessage(IconMessages.noConnectionMessage)
        }
    }

    fun cancelAd() {
        rewards.cancel()
        onEvent(SelectIconEvent.UpdateDialog())
    }

    private fun reward(icon: IconResource, context: Context) =
        viewModelScope.launch(Dispatchers.IO) {
            interactor.unlock(context, icon.name(context.resources))
            withContext(Dispatchers.Main) {
                onEvent(SelectIconEvent.UpdateDialog())
                onEvent(SelectIconEvent.Update(icon = icon))
                showMessage(IconMessages.rewardedMessage(icon))
            }
        }

    private fun rewardFailed() {
        onEvent(SelectIconEvent.UpdateDialog())
        showMessage(IconMessages.failedToLoadRewardMessage)
    }
}