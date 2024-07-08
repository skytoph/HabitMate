package com.github.skytoph.taski.presentation.habit.list

import androidx.compose.runtime.MutableState
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.view.FilterOption
import com.github.skytoph.taski.presentation.habit.list.view.SortOption
import com.github.skytoph.taski.presentation.habit.list.view.ViewOption
import com.github.skytoph.taski.presentation.settings.SettingsViewModel

interface HabitListEvent {
    fun handle(state: MutableState<HabitListState>)

    class UpdateList(private val habits: List<HabitWithHistoryUi<HistoryUi>>) : HabitListEvent {
        override fun handle(state: MutableState<HabitListState>) {
            state.value = state.value.copy(habits = habits, isLoading = false)
        }
    }

    class UpdateView(
        private val viewType: ViewOption? = null,
        private val sortBy: SortOption? = null,
        private val filterBy: FilterOption? = null
    ) : SettingsViewModel.Event {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateView(viewType, sortBy, filterBy)
        }
    }

    class UpdateEntries(private val entries: Int) : SettingsViewModel.Event {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateViewNumberOfEntries(entries)
        }
    }

    object Progress : HabitListEvent {
        override fun handle(state: MutableState<HabitListState>) {
            state.value = state.value.copy(isLoading = true)
        }
    }

    class ShowViewType(private val show: Boolean) : HabitListEvent {
        override fun handle(state: MutableState<HabitListState>) {
            state.value = state.value.copy(isViewTypeVisible = show)
        }
    }

    class UpdateContextMenu(private val id: Long? = null) : HabitListEvent {
        override fun handle(state: MutableState<HabitListState>) {
            state.value = state.value.copy(contextMenuHabitId = id)
        }
    }

    class ShowDeleteDialog(private val id: Long? = null) : HabitListEvent {
        override fun handle(state: MutableState<HabitListState>) {
            state.value = state.value.copy(deleteDialogHabitId = id)
        }
    }

    class ShowArchiveDialog(private val id: Long? = null) : HabitListEvent {
        override fun handle(state: MutableState<HabitListState>) {
            state.value = state.value.copy(archiveDialogHabitId = id)
        }
    }
}