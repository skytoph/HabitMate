package com.github.skytoph.taski.presentation.habit.create

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

interface SelectIcon {
    fun selectIcon(icon: ImageVector? = null, color: Color? = null)
    fun state(): State<EditHabitState>
}