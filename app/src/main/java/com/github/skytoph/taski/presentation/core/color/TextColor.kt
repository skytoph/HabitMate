package com.github.skytoph.taski.presentation.core.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun Color.contrastColor(): Color =
    if (ColorUtils.calculateLuminance(this.toArgb()) < 0.5) Color.White else Color.Black

fun Color.borderColor(background: Color): Color =
    if (ColorUtils.calculateLuminance(background.toArgb()) > 0.7f)
        Color.White
    else
        background.copy(alpha = 0.9f - ColorUtils.calculateLuminance(toArgb()).toFloat()).compositeOver(this)
