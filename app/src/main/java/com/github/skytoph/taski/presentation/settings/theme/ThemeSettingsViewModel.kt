package com.github.skytoph.taski.presentation.settings.theme

import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.settings.SettingsViewModel
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