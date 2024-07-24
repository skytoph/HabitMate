package com.github.skytoph.taski.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.core.datastore.Settings
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class SettingsViewModel<E : SettingsViewModel.Event>(
    private val settings: SettingsCache,
    initAppBar: InitAppBar
) : ViewModel(), InitAppBar by initAppBar, ProvideState by ProvideState.Base(settings) {

    fun onEvent(event: E) = viewModelScope.launch(Dispatchers.IO) {
        event.handle(settings)
    }

    interface Event {
        suspend fun handle(settings: SettingsCache)
    }
}

abstract class InitStateViewModel(private val settings: SettingsCache) : ViewModel(),
    ProvideState by ProvideState.Base(settings) {

    fun initState() = viewModelScope.launch(Dispatchers.IO) {
        settings.initialize()
    }
}

interface ProvideState {
    fun settings(): StateFlow<Settings>

    class Base(private val settings: SettingsCache) : ProvideState {
        override fun settings(): StateFlow<Settings> = settings.state()
    }
}