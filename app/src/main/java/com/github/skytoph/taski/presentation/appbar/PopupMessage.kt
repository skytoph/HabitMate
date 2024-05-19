package com.github.skytoph.taski.presentation.appbar

import androidx.compose.material3.SnackbarHostState

interface PopupMessage {
    interface Show<M> {
        suspend fun show(message: M)
    }

    interface Provide<S> {
        fun provide(): S
    }

    interface Mutable<M, S> : Show<M>, Provide<S>

    class Snackbar(private val state: SnackbarHostState) :
        Mutable<SnackbarMessage, SnackbarHostState> {

        override suspend fun show(message: SnackbarMessage) {
            state.showSnackbar(message)
        }

        override fun provide(): SnackbarHostState = state
    }
}