package com.github.skytoph.taski.presentation.settings.menu

import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsMenuViewModel @Inject constructor(
    settings: SettingsCache,
    initAppBar: InitAppBar
) : SettingsViewModel<SettingsViewModel.Event>(settings, initAppBar)