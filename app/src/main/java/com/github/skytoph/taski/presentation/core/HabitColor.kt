package com.github.skytoph.taski.presentation.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun habitColor(colorPercent: Float, defaultColor: Color, habitColor: Color) =
    when {
        colorPercent <= 0F -> defaultColor
        colorPercent >= 1F -> habitColor
        else -> Color(
            ColorUtils.blendARGB(defaultColor.toArgb(), habitColor.toArgb(), colorPercent)
        )
    }

fun borderColor(habitColor: Color) = Color(
    ColorUtils.setAlphaComponent(
        ColorUtils.blendARGB(Color.Black.toArgb(), habitColor.toArgb(), 0.4F), 0xBF
    )
)