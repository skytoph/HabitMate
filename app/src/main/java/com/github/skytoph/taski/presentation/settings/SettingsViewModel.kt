package com.github.skytoph.taski.presentation.settings

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.core.component.AppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(state: MutableState<AppBarState>) : ViewModel(),
    InitAppBar by InitAppBar.Base(state)