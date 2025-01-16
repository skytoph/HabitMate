package com.github.skytoph.taski.presentation.habit.icon

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.settings.SettingsViewModel

interface SelectIconEvent {
    fun handle(iconState: MutableState<IconState>, state: MutableState<SelectIconState>? = null)

    class Initialize(private val icons: List<IconsLockedGroup>) : SelectIconEvent {
        override fun handle(iconState: MutableState<IconState>, state: MutableState<SelectIconState>?) {
            state?.let { state.value = state.value.copy(icons = icons, isLoading = false) }
        }
    }

    class Update(
        private val icon: IconResource? = null,
        private val color: Color? = null
    ) : SelectIconEvent {
        override fun handle(iconState: MutableState<IconState>, state: MutableState<SelectIconState>?) {
            icon?.let { iconState.value = iconState.value.copy(icon = icon) }
            color?.let { iconState.value = iconState.value.copy(color = color) }
        }
    }

    class UpdateDialog(private val icon: IconResource? = null, private val isLoading: Boolean = false) :
        SelectIconEvent {
        override fun handle(iconState: MutableState<IconState>, state: MutableState<SelectIconState>?) {
            if (isLoading) state?.let { state.value = state.value.copy(isDialogLoading = true) }
            else state?.let { state.value = state.value.copy(dialogIcon = icon, isDialogLoading = false) }
        }
    }

    class IsSigningIn(private val isSigningIn: Boolean) : SelectIconEvent {
        override fun handle(iconState: MutableState<IconState>, state: MutableState<SelectIconState>?) {
            state?.let { state.value = state.value.copy(isSigningIn = isSigningIn) }
        }
    }

    class IsWarningShown(private val isShown: Boolean) : SelectIconEvent {
        override fun handle(iconState: MutableState<IconState>, state: MutableState<SelectIconState>?) {
            state?.let { state.value = state.value.copy(isWarningShown = isShown) }
        }
    }

    class IsWarningDialogShown(private val isShown: Boolean) : SelectIconEvent {
        override fun handle(iconState: MutableState<IconState>, state: MutableState<SelectIconState>?) {
            state?.let { state.value = state.value.copy(isWarningDialogShown = isShown) }
        }
    }

    interface SettingsEvent : SettingsViewModel.Event

    class UpdateSort : SettingsEvent {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateIconsSort()
        }
    }

    class UpdateLastSync(private val time: Long?) : SettingsEvent {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateBackupTime(time)
        }
    }

    object DoNotShowWarning : SettingsEvent {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateIconWarning(false)
        }
    }

    object Clear : SelectIconEvent {
        override fun handle(iconState: MutableState<IconState>, state: MutableState<SelectIconState>?) {
            iconState.value = IconState()
        }
    }
} 