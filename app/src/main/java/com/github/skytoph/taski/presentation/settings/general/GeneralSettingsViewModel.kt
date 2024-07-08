package com.github.skytoph.taski.presentation.settings.general

import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GeneralSettingsViewModel @Inject constructor(settings: SettingsCache, initAppBar: InitAppBar) :
    SettingsViewModel<GeneralSettingsEvent>(settings, initAppBar)