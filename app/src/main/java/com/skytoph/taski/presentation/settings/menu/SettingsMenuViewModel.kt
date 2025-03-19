package com.skytoph.taski.presentation.settings.menu

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.skytoph.taski.core.datastore.CrashlyticsIdDataStore
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.presentation.appbar.InitAppBar
import com.skytoph.taski.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsMenuViewModel @Inject constructor(
    private val idDataSource: CrashlyticsIdDataStore,
    settings: SettingsCache,
    initAppBar: InitAppBar
) : SettingsViewModel<SettingsViewModel.Event>(settings, initAppBar) {

    fun sendFeedback(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val id = idDataSource.id()
        withContext(Dispatchers.Main) {
            sendFeedback(context, id)
        }
    }
}