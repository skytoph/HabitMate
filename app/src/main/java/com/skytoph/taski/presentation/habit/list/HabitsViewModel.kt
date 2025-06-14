package com.skytoph.taski.presentation.habit.list

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.core.datastore.settings.HabitsView
import com.skytoph.taski.domain.habit.HabitWithEntries
import com.skytoph.taski.presentation.appbar.InitAppBar
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.skytoph.taski.presentation.habit.list.mapper.HabitsViewMapper
import com.skytoph.taski.presentation.settings.SettingsViewModel
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

    val view = settings()

    init {
        onEvent(HabitListEvent.Progress)
        interactor.habits()
            .combine(view) { habits, viewState -> applyViewState(habits, viewState.view) }
            .flowOn(Dispatchers.IO)
            .onEach { habits ->
                habits?.let {
                    onEvent(HabitListEvent.UpdateList(habits, settings().value.allowCrashlytics))
                }
            }.launchIn(viewModelScope)
    }

    private fun applyViewState(habits: List<HabitWithEntries>, viewState: HabitsView)
            : List<HabitWithHistoryUi<HistoryUi>>? = viewState.map(mapper, habits, settings().value)

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