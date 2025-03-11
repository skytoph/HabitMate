package com.skytoph.taski.presentation.appbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.skytoph.taski.presentation.core.component.AppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppBarViewModel @Inject constructor(
    private val state: MutableState<AppBarState>,
    private val popup: PopupMessage.Provide<SnackbarHostState>
) : ViewModel() {

    fun initState(color: Color) = AppBarEvent.InitNavigateUp(color)

    fun state(): State<AppBarState> = state

    fun expandList(expand: Boolean) = AppBarEvent.ExpandList(expand).handle(state)

    fun snackbarState(): SnackbarHostState = popup.provide()
}