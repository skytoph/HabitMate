package com.github.skytoph.taski.core.datastore

import androidx.datastore.core.DataStore
import com.github.skytoph.taski.presentation.habit.list.view.FilterHabits
import com.github.skytoph.taski.presentation.habit.list.view.SortHabits
import com.github.skytoph.taski.presentation.habit.list.view.ViewType
import com.github.skytoph.taski.presentation.settings.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface SettingsCache {
    fun state(): StateFlow<Settings>
    suspend fun initAndGet(): Flow<Settings>
    suspend fun initialize()
    suspend fun update(settings: Settings)
    suspend fun updateWeekStart()
    suspend fun updateCurrentDayHighlight()
    suspend fun updateStreakHighlight()
    suspend fun updateIconsSort()
    suspend fun updateCrashlytics(allow: Boolean? = null)
    suspend fun updateTheme(theme: AppTheme)
    suspend fun updateViewNumberOfEntries(number: Int)
    suspend fun updateBackupTime(time: Long?)
    suspend fun updateIconWarning(show: Boolean? = null)
    suspend fun updateView(
        viewType: ViewType? = null, sortBy: SortHabits? = null, filterBy: FilterHabits? = null,
        showTodayHabitsOnly: Boolean? = null
    )

    class Base(
        private val dataStore: DataStore<Settings>,
        private val state: MutableStateFlow<Settings> = MutableStateFlow(Settings.notInitialized)
    ) : SettingsCache {

        override suspend fun initialize() {
            dataStore.data.collect { state.value = it }
        }

        override suspend fun update(settings: Settings) {
            dataStore.updateData { settings.copy(lastBackupSaved = it.lastBackupSaved) }
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

        override suspend fun updateIconsSort() {
            dataStore.updateData { it.copy(sortIcons = !it.sortIcons) }
        }

        override suspend fun updateCrashlytics(allow: Boolean?) {
            dataStore.updateData { it.copy(allowCrashlytics = allow ?: it.allowCrashlytics?.not() ?: true) }
        }

        override suspend fun updateTheme(theme: AppTheme) {
            dataStore.updateData { it.copy(theme = theme) }
        }

        override suspend fun updateBackupTime(time: Long?) {
            dataStore.updateData { it.copy(lastBackupSaved = time) }
        }

        override suspend fun updateIconWarning(show: Boolean?) {
            if (show == null) dataStore.updateData { it.copy(showIconWarning = !it.showIconWarning) }
            else dataStore.updateData { it.copy(showIconWarning = show) }
        }

        override suspend fun updateView(
            viewType: ViewType?, sortBy: SortHabits?, filterBy: FilterHabits?,
            showTodayHabitsOnly: Boolean?
        ) {
            viewType?.let { dataStore.updateData { it.copy(view = it.view.copy(viewType = viewType)) } }
            sortBy?.let { dataStore.updateData { it.copy(view = it.view.copy(sortBy = sortBy)) } }
            filterBy?.let { dataStore.updateData { it.copy(view = it.view.copy(filterBy = filterBy)) } }
            showTodayHabitsOnly?.let { dataStore.updateData { it.copy(view = it.view.copy(showTodayHabitsOnly = showTodayHabitsOnly)) } }
        }

        override suspend fun updateViewNumberOfEntries(number: Int) {
            dataStore.updateData {
                val item = it.view.viewType.withEntries(number)
                it.copy(view = it.view.copy(viewType = item))
            }
        }

        override fun state(): StateFlow<Settings> = state

        override suspend fun initAndGet(): Flow<Settings> = dataStore.data
    }
}