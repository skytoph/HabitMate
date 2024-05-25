package com.github.skytoph.taski.presentation.appbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import com.github.skytoph.taski.presentation.core.state.IconResource

data class SnackbarMessage(
    override val message: String,
    val title: String,
    val icon: IconResource,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    override val withDismissAction: Boolean = false
) : SnackbarVisuals