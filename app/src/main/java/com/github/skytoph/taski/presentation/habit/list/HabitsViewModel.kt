package com.github.skytoph.taski.presentation.habit.list

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.domain.habit.HabitWithEntries
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitsViewMapper
import com.github.skytoph.taski.presentation.habit.list.view.HabitsView
import com.github.skytoph.taski.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    settings: SettingsCache,
    initAppBar: InitAppBar
) : SettingsViewModel<SettingsViewModel.Event>(settings, initAppBar) {

    val view = state()

    init {
        onEvent(HabitListEvent.Progress)
        interactor.habits()
            .combine(view) { habits, viewState -> applyViewState(habits, viewState.view) }
            .flowOn(Dispatchers.IO)
            .onEach { habits -> onEvent(HabitListEvent.UpdateList(habits)) }
            .launchIn(viewModelScope)
    }

    private fun applyViewState(habits: List<HabitWithEntries>, viewState: HabitsView) =
        viewState.map(mapper, habits)

    fun habitDone(habit: HabitUi, daysAgo: Int = 0) = viewModelScope.launch(Dispatchers.IO) {
        interactor.habitDone(habit, daysAgo)
    }

    fun onEvent(event: HabitListEvent) = event.handle(state)

    fun habitsState(): State<HabitListState> = state

    fun deleteHabit(id: Long, message: String, context: Context) = viewModelScope.launch(Dispatchers.IO) {
        interactor.delete(id, message, context)
    }

    fun archiveHabit(id: Long, archived: String) = viewModelScope.launch(Dispatchers.IO) {
        interactor.archive(id, archived)
    }
}