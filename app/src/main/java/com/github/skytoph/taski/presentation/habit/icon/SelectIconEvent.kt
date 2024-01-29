package com.github.skytoph.taski.presentation.habit.icon

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

interface SelectIconEvent {
    fun handle(state: MutableState<IconState>)

    class Update(
        private val icon: ImageVector? = null,
        private val color: Color? = null
    ) : SelectIconEvent {
        override fun handle(state: MutableState<IconState>) {
            icon?.let { state.value = state.value.copy(icon = icon) }
            color?.let { state.value = state.value.copy(color = color) }
        }
    }

    object Clear : SelectIconEvent {
        override fun handle(state: MutableState<IconState>) {
            state.value = IconState()
        }
    }
}