package com.github.skytoph.taski.core.datastore

import androidx.datastore.core.DataStore
import com.github.skytoph.taski.presentation.habit.list.view.FilterHabits
import com.github.skytoph.taski.presentation.habit.list.view.SortHabits
import com.github.skytoph.taski.presentation.habit.list.view.ViewType
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
    suspend fun updateView(viewType: ViewType? = null, sortBy: SortHabits? = null, filterBy: FilterHabits? = null)
    suspend fun updateViewNumberOfEntries(number: Int)

    class Base(
        private val dataStore: DataStore<Settings>,
        private val state: MutableStateFlow<Settings> = MutableStateFlow(Settings.notInitialized)
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

        override suspend fun updateView(viewType: ViewType?, sortBy: SortHabits?, filterBy: FilterHabits?) {
            viewType?.let { dataStore.updateData { it.copy(view = it.view.copy(viewType = viewType)) } }
            sortBy?.let { dataStore.updateData { it.copy(view = it.view.copy(sortBy = sortBy)) } }
            filterBy?.let { dataStore.updateData { it.copy(view = it.view.copy(filterBy = filterBy)) } }
        }

        override suspend fun updateViewNumberOfEntries(number: Int) {
            dataStore.updateData {
                val item = it.view.viewType.withEntries(number)
                it.copy(view = it.view.copy(viewType = item))
            }
        }

        override fun state(): StateFlow<Settings> = state
    }
}