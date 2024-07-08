package com.github.skytoph.taski.core.datastore

import androidx.datastore.core.DataStore
import com.github.skytoph.taski.presentation.settings.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface SettingsCache {
    fun state(): StateFlow<Settings>
    suspend fun initialize()
    suspend fun updateWeekStart()
    suspend fun updateCurrentDayHighlight()
    suspend fun updateStreakHighlight()
    suspend fun updateTheme(theme: AppTheme)

    class Base(
        private val dataStore: DataStore<Settings>,
        private val mapper: InitializeEmptyValues,
        private val state: MutableStateFlow<Settings> = MutableStateFlow(mapper.initialize(Settings()))
    ) : SettingsCache {

        override suspend fun initialize() {
            dataStore.data.collect { state.value = it }
        }

        override suspend fun updateWeekStart() {
            dataStore.updateData { it.copy(weekStartsOnSunday = it.weekStartsOnSunday.copy(!it.weekStartsOnSunday.value)) }
        }

        override suspend fun updateCurrentDayHighlight() {
            dataStore.updateData { it.copy(currentDayHighlighted = !it.currentDayHighlighted) }
        }

        override suspend fun updateStreakHighlight() {
            dataStore.updateData { it.copy(streaksHighlighted = !it.streaksHighlighted) }
        }

        override suspend fun updateTheme(theme: AppTheme) {
            dataStore.updateData { it.copy(theme = theme) }
        }

        override fun state(): StateFlow<Settings> = state
    }
}