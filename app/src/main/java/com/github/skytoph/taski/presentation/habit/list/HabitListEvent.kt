package com.github.skytoph.taski.presentation.habit.list

import androidx.compose.runtime.MutableState
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.view.FilterOption
import com.github.skytoph.taski.presentation.habit.list.view.HabitsView
import com.github.skytoph.taski.presentation.habit.list.view.SortOption
import com.github.skytoph.taski.presentation.habit.list.view.ViewOption
import kotlinx.coroutines.flow.MutableStateFlow

interface HabitListEvent {
    fun handle(state: MutableState<HabitListState>, view: MutableStateFlow<HabitsView>)

    class UpdateList(private val habits: List<HabitWithHistoryUi<HistoryUi>>) : HabitListEvent {
        override fun handle(
            state: MutableState<HabitListState>, view: MutableStateFlow<HabitsView>
        ) {
            state.value = state.value.copy(habits = habits, isLoading = false)
        }
    }

    class UpdateView(
        private val viewType: ViewOption? = null,
        private val sortBy: SortOption? = null,
        private val filterBy: FilterOption? = null
    ) : HabitListEvent {
        override fun handle(
            state: MutableState<HabitListState>, view: MutableStateFlow<HabitsView>
        ) {
            viewType?.let { view.value = view.value.copy(viewType = viewType) }
            sortBy?.let { view.value = view.value.copy(sortBy = sortBy) }
            filterBy?.let { view.value = view.value.copy(filterBy = filterBy) }
        }

    }

    class UpdateEntries(private val entries: Int) : HabitListEvent {
        override fun handle(
            state: MutableState<HabitListState>, view: MutableStateFlow<HabitsView>
        ) {
            val item = view.value.viewType.item.withEntries(entries)
            view.value = view.value.copy(viewType = view.value.viewType.copy(item = item))
        }
    }

    object Progress : HabitListEvent {
        override fun handle(
            state: MutableState<HabitListState>, view: MutableStateFlow<HabitsView>
        ) {
            state.value = state.value.copy(isLoading = true)
        }
    }

    class ShowViewType(private val show: Boolean) : HabitListEvent {
        override fun handle(
            state: MutableState<HabitListState>, view: MutableStateFlow<HabitsView>
        ) {
            state.value = state.value.copy(isViewTypeVisible = show)
        }
    }

    class UpdateContextMenu(private val id: Long? = null) : HabitListEvent {
        override fun handle(
            state: MutableState<HabitListState>, view: MutableStateFlow<HabitsView>
        ) {
            state.value = state.value.copy(contextMenuHabitId = id)
        }
    }

    class ShowDeleteDialog(private val id: Long? = null) : HabitListEvent {
        override fun handle(
            state: MutableState<HabitListState>, view: MutableStateFlow<HabitsView>
        ) {
            state.value = state.value.copy(deleteDialogHabitId = id)
        }
    }
}