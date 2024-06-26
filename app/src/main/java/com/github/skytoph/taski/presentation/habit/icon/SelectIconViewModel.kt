package com.github.skytoph.taski.presentation.habit.icon

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.core.component.AppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectIconViewModel @Inject constructor(
    private val state: MutableState<IconState>,
    appBarState: MutableState<AppBarState>
) : ViewModel(), InitAppBar by InitAppBar.Base(appBarState) {

    fun onEvent(event: SelectIconEvent) = event.handle(state)

    fun state(): State<IconState> = state
}