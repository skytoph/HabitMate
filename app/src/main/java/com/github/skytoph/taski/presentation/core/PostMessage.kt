package com.github.skytoph.taski.presentation.core

import com.github.skytoph.taski.presentation.appbar.SnackbarMessage

fun interface PostMessage {
    fun postMessage(message: SnackbarMessage)
}