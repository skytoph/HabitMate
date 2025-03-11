package com.skytoph.taski.presentation.habit.icon

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class IconsLockedGroup(@StringRes val titleResId: Int, override val icons: List<Pair<Int, Boolean>>) :
    IconsGroup<Pair<Int, Boolean>>(titleResId)