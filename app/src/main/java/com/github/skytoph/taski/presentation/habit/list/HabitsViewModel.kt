package com.github.skytoph.taski.presentation.habit.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.core.component.AppBarState
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitWithHistoryUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val state: MutableState<HabitListState>,
    private val mapper: HabitWithHistoryUiMapper<HistoryUi>,
    private val interactor: HabitListInteractor,
    appBarState: MutableState<AppBarState>
) : ViewModel(), InitAppBar by InitAppBar.Base(appBarState) {

    init {
        onEvent(HabitListEvent.Progress)
        interactor.habits()
            .map { habits -> habits.map { habit -> habit.map(mapper) } }
            .flowOn(Dispatchers.IO)
            .onEach { habits -> onEvent(HabitListEvent.UpdateList(habits)) }
            .launchIn(viewModelScope)
    }

    fun habitDone(habit: HabitUi) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.habitDone(habit)
        }
    }

    fun onEvent(event: HabitListEvent) = event.handle(state)

    fun state(): State<HabitListState> = state
}