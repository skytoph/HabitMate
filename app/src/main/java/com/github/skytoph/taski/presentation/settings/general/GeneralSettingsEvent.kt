package com.github.skytoph.taski.presentation.settings.general

import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.settings.SettingsViewModel

sealed interface GeneralSettingsEvent : SettingsViewModel.Event {

    data object ToggleWeekStart : GeneralSettingsEvent {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateWeekStart()
        }
    }

    data object ToggleCurrentDayHighlight : GeneralSettingsEvent {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateCurrentDayHighlight()
        }
    }

    data object ToggleStreakHighlight : GeneralSettingsEvent {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateStreakHighlight()
        }
    }

    data object ToggleIconWarning : GeneralSettingsEvent {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateIconWarning()
        }
    }

    data object ToggleAllowCrashlytics : GeneralSettingsEvent {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateCrashlytics()
        }
    }
}