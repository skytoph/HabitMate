package com.skytoph.taski.presentation.appbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource

data class SnackbarMessage(
    val messageResource: StringResource,
    val title: StringResource,
    val icon: IconResource,
    val isError: Boolean = false,
    val color: Int? = null,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    override val withDismissAction: Boolean = false,
    override val message: String = ""
) : SnackbarVisuals