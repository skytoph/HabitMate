package com.skytoph.taski.presentation.settings.reorder

import androidx.compose.runtime.MutableState
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.core.datastore.settings.SortHabits
import com.skytoph.taski.presentation.settings.SettingsViewModel

sealed interface ReorderHabitsEvent {
    fun handle(state: MutableState<ReorderState>) = Unit

    data object ApplyManualOrder : SettingsEvent {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateView(sortBy = SortHabits.Manually)
        }
    }

    data class UpdateReminder(private val show: Boolean) : ReorderHabitsEvent {
        override fun handle(state: MutableState<ReorderState>) {
            state.value = state.value.copy(isReminderShown = show)
        }
    }

    interface SettingsEvent : ReorderHabitsEvent, SettingsViewModel.Event
}