package com.github.skytoph.taski.presentation.habit.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.core.component.AppBarState
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitsViewMapper
import com.github.skytoph.taski.presentation.habit.list.view.HabitsView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val state: MutableState<HabitListState>,
    private val mapper: HabitsViewMapper,
    private val interactor: HabitListInteractor,
    appBarState: MutableState<AppBarState>
) : ViewModel(), InitAppBar by InitAppBar.Base(appBarState) {

    val view = MutableStateFlow(HabitsView())

    init {
        onEvent(HabitListEvent.Progress)
        interactor.habits()
            .combine(view) { habits, viewState -> applyViewState(habits, viewState) }
            .flowOn(Dispatchers.IO)
            .onEach { habits -> onEvent(HabitListEvent.UpdateList(habits)) }
            .launchIn(viewModelScope)
    }

    private fun applyViewState(habits: List<HabitWithEntries>, viewState: HabitsView) =
        viewState.map(mapper, habits)

    fun habitDone(habit: HabitUi, daysAgo: Int = 0) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.habitDone(habit, daysAgo)
        }
    }

    fun onEvent(event: HabitListEvent) = event.handle(state, view)

    fun state(): State<HabitListState> = state

    fun deleteHabit(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        interactor.delete(id)
    }
}