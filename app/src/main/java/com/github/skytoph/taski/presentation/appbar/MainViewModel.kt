package com.github.skytoph.taski.presentation.appbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.github.skytoph.taski.presentation.core.component.AppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val state: MutableState<AppBarState>,
    private val popup: PopupMessage.Provide<SnackbarHostState>
) : ViewModel() {

    fun initState(color: Color) = AppBarEvent.InitNavigateUp(color)

    fun state(): State<AppBarState> = state

    fun snackbarState(): SnackbarHostState = popup.provide()
}