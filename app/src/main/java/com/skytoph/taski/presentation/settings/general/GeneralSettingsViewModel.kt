package com.skytoph.taski.presentation.settings.general

import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.presentation.appbar.InitAppBar
import com.skytoph.taski.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GeneralSettingsViewModel @Inject constructor(settings: SettingsCache, initAppBar: InitAppBar) :
    SettingsViewModel<GeneralSettingsEvent>(settings, initAppBar)