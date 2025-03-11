@file:OptIn(ExperimentalMaterial3Api::class)

package com.skytoph.taski.presentation.nav

import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable

@Composable
fun rememberDismissState(state: SnackbarHostState): DismissState =
    rememberDismissState(confirmValueChange = { value: DismissValue ->
        if (value != DismissValue.Default) {
            state.currentSnackbarData?.dismiss()
            true
        } else false
    })