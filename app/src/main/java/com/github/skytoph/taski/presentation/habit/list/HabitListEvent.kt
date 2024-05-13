package com.github.skytoph.taski.presentation.habit.list

import androidx.compose.runtime.MutableState
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
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

    class UpdateView(private val viewState: HabitsView) : HabitListEvent {
        override fun handle(
            state: MutableState<HabitListState>, view: MutableStateFlow<HabitsView>
        ) {
            view.value = viewState
        }
    }

    object Progress : HabitListEvent {
        override fun handle(
            state: MutableState<HabitListState>, view: MutableStateFlow<HabitsView>
        ) {
            state.value = state.value.copy(isLoading = true)
        }
    }

    object ShowMenu : HabitListEvent {
        override fun handle(
            state: MutableState<HabitListState>, view: MutableStateFlow<HabitsView>
        ) {
            state.value = state.value.copy(isViewTypeVisible = state.value.isViewTypeVisible.not())
        }
    }
}