@file:OptIn(ExperimentalMaterial3Api::class)

package com.skytoph.taski.presentation.nav

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable

@Composable
fun rememberDismissState(state: SnackbarHostState): SwipeToDismissBoxState =
    rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value != SwipeToDismissBoxValue.Settled) {
                state.currentSnackbarData?.dismiss()
                true
            } else false
        })