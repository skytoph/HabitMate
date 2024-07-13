package com.github.skytoph.taski.presentation.habit.list.view

import androidx.compose.runtime.Stable
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.core.state.StringResource

@Stable
data class OptionUi(
    val icon: IconResource,
    val title: StringResource,
)