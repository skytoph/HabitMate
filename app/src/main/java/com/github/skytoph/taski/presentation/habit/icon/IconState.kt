package com.github.skytoph.taski.presentation.habit.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class IconState(
    val icon: ImageVector = IconsGroup.allGroups.first().icons.first(),
    val color: Color = IconsColors.allColors.first()
)