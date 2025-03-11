package com.skytoph.taski.presentation.settings.theme

import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.core.datastore.settings.AppTheme
import com.skytoph.taski.presentation.appbar.InitAppBar
import com.skytoph.taski.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeSettingsViewModel @Inject constructor(settings: SettingsCache, initAppBar: InitAppBar) :
    SettingsViewModel<ThemeSettingsEvent>(settings, initAppBar)

interface ThemeSettingsEvent : SettingsViewModel.Event {

    class SelectTheme(private val theme: AppTheme) : ThemeSettingsEvent {
        override suspend fun handle(settings: SettingsCache) {

            settings.updateTheme(theme)
        }
    }
}