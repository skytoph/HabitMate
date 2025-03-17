package com.skytoph.taski.presentation.settings.general

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.skytoph.taski.core.datastore.CrashlyticsIdDataStore
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.presentation.appbar.InitAppBar
import com.skytoph.taski.presentation.appbar.PopupMessage
import com.skytoph.taski.presentation.appbar.SnackbarMessage
import com.skytoph.taski.presentation.habit.icon.RewardDataSource
import com.skytoph.taski.presentation.settings.SettingsViewModel
import com.skytoph.taski.presentation.settings.backup.GeneralSettingsMessages.idCopiedMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GeneralSettingsViewModel @Inject constructor(
    private val reward: RewardDataSource,
    private val state: MutableState<GeneralSettingsState>,
    private val snackbar: PopupMessage.Show<SnackbarMessage>,
    private val idDataSource: CrashlyticsIdDataStore,
    settings: SettingsCache,
    initAppBar: InitAppBar
) : SettingsViewModel<GeneralSettingsEvent.UpdateSettings>(settings, initAppBar) {

    fun init(activity: Activity) {
        onEvent(GeneralSettingsEvent.UpdatePrivacyVisibility(reward.isPrivacyOptionsRequired(activity)))
    }

    fun onEvent(event: GeneralSettingsEvent.UpdateState) = event.handle(state)

    fun openPrivacySettings(activity: Activity) {
        reward.showPrivacyOptions(activity)
    }

    fun state(): State<GeneralSettingsState> = state

    fun showMessage(message: SnackbarMessage) {
        viewModelScope.launch { snackbar.show(message) }
    }

    fun copyId(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val id = idDataSource.id()
        withContext(Dispatchers.Main) {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Support ID", id)
            clipboardManager.setPrimaryClip(clipData)
            showMessage(idCopiedMessage)
        }
    }
}