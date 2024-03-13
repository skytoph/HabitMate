package com.github.skytoph.taski.presentation.habit.icon

import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.presentation.core.state.IconResource

data class IconState(
    val icon: IconResource = IconResource.Default,
    val color: Color = IconsColors.Default
)