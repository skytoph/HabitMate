package com.skytoph.taski.presentation.settings.general

import androidx.compose.runtime.MutableState
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.presentation.settings.SettingsViewModel

sealed interface GeneralSettingsEvent {

    interface UpdateSettings : SettingsViewModel.Event
    interface UpdateState {
        fun handle(state: MutableState<GeneralSettingsState>)
    }

    data class UpdatePrivacyVisibility(private val isVisible: Boolean) : UpdateState {
        override fun handle(state: MutableState<GeneralSettingsState>) {
            state.value = state.value.copy(isPrivacySettingVisible = isVisible)
        }
    }

    data object ToggleWeekStart : UpdateSettings {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateWeekStart()
        }
    }

    data object ToggleTimeFormat : UpdateSettings {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateTimeFormat()
        }
    }

    data object ToggleCurrentDayHighlight : UpdateSettings {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateCurrentDayHighlight()
        }
    }

    data object ToggleStreakHighlight : UpdateSettings {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateStreakHighlight()
        }
    }

    data object ToggleIconWarning : UpdateSettings {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateIconWarning()
        }
    }

    data object ToggleAllowCrashlytics : UpdateSettings {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateCrashlytics()
        }
    }
}