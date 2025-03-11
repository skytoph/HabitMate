package com.skytoph.taski.presentation.core

import com.skytoph.taski.presentation.appbar.SnackbarMessage

fun interface PostMessage {
    fun postMessage(message: SnackbarMessage)
}