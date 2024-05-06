package com.github.skytoph.taski.presentation.nav

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.presentation.core.component.AppBarAction
import com.github.skytoph.taski.presentation.core.component.AppBarState
import com.github.skytoph.taski.presentation.core.state.StringResource

sealed class AppBarEvent {
    abstract fun handle(state: MutableState<AppBarState>)

    class InitNavigateUp(private val color: Color) : AppBarEvent() {
        override fun handle(state: MutableState<AppBarState>) {
            state.value = state.value.copy(
                navigateUp = state.value.navigateUp.copy(
                    action = state.value.navigateUp.action.copy(color = color)
                )
            )
        }
    }

    class Update(
        private val title: StringResource = StringResource.Value(""),
        private val menuItems: List<AppBarAction> = emptyList(),
        private val dropdownItems: List<AppBarAction> = emptyList(),
        private val canNavigateUp: Boolean = true
    ) : AppBarEvent() {
        override fun handle(state: MutableState<AppBarState>) {
            state.value = state.value.copy(
                title = title,
                menuItems = menuItems,
                dropdownItems = dropdownItems,
                navigateUp = state.value.navigateUp.copy(canNavigateUp = canNavigateUp)
            )
        }
    }
}