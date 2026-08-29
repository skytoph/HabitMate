package com.skytoph.taski.mock.data

import androidx.compose.ui.graphics.Color
import com.skytoph.taski.presentation.habit.icon.IconsColors

object HabitColorData {
    fun getRandom(): Color = IconsColors.allColors.random()
}