package com.github.skytoph.taski.presentation.habit.icon

import com.github.skytoph.taski.presentation.core.state.IconResource

data class SelectIconState(
    val icons: List<IconsLockedGroup> = emptyList(),
    val isLoading: Boolean = true,
    val isSigningIn: Boolean = false,
    val dialogIcon: IconResource? = null,
    val isWarningShown: Boolean = false,
    val isWarningDialogShown: Boolean = false,
)