package com.github.skytoph.taski

import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.settings.InitStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(settings: SettingsCache) : InitStateViewModel(settings)