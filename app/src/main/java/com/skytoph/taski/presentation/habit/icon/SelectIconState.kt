package com.skytoph.taski.presentation.habit.icon

import com.skytoph.taski.presentation.core.state.IconResource

data class SelectIconState(
    val icons: List<IconsLockedGroup> = emptyList(),
    val isLoading: Boolean = true,
    val isSigningIn: Boolean = false,
    val dialogIcon: IconResource? = null,
    val isDialogLoading: Boolean = false,
    val isDialogShown: Boolean = false,
    val isWarningShown: Boolean = false,
    val isWarningDialogShown: Boolean = false,
    val isRewardErrorDialogShown: Boolean = false,
)