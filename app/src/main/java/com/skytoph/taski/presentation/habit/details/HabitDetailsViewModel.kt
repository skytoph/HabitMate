package com.skytoph.taski.presentation.habit.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.presentation.appbar.InitAppBar
import com.skytoph.taski.presentation.habit.HabitScreens
import com.skytoph.taski.presentation.habit.details.mapper.StatisticsUiMapper
import com.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import com.skytoph.taski.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HabitDetailsViewModel @Inject constructor(
    private val state: MutableStateFlow<HabitDetailsState> = MutableStateFlow(HabitDetailsState()),
    private val interactor: HabitDetailsInteractor,
    private val habitMapper: HabitUiMapper,
    private val statsMapper: StatisticsUiMapper,
    savedStateHandle: SavedStateHandle,
    settings: SettingsCache,
    initAppBar: InitAppBar
) : SettingsViewModel<SettingsViewModel.Event>(settings, initAppBar) {

    private val actions = MutableStateFlow<List<HabitEntriesAction>>(emptyList())

    val entries: Flow<PagingData<EditableHistoryUi>> =
        interactor.entries(savedStateHandle.id())
            .cachedIn(viewModelScope)
            .combine(actions) { pagingData, actions ->
                actions.fold(pagingData) { paging, event -> applyAction(paging, event) }
            }

    init {
        interactor.habit(savedStateHandle.id())
            .map { habit -> habit?.let { habitMapper.map(it) } }
            .flowOn(Dispatchers.IO)
            .onEach { it?.let { habit -> onEvent(HabitDetailsEvent.Init(habit)) } }
            .launchIn(viewModelScope)

        interactor.statistics(savedStateHandle.id())
            .map { history -> history?.let { statsMapper.map(it, settings().value.weekStartsOnSunday.value) } }
            .flowOn(Dispatchers.IO)
            .onEach { stats ->
                stats?.let {
                    onEvent(HabitDetailsEvent.UpdateStats(it))
                    HabitEntriesAction.ApplyStatistics(stats, settings().value.streaksHighlighted).handle(actions)
                }
            }.launchIn(viewModelScope)
    }

    private fun applyAction(pagingData: PagingData<EditableHistoryUi>, action: HabitEntriesAction) =
        pagingData.map { data -> action.apply(data) }

    fun habitDone(daysAgo: Int) = state.value.habit?.let { habit ->
        viewModelScope.launch(Dispatchers.IO) {
            val entry = interactor.habitDoneAndReturn(habit, daysAgo)
            withContext(Dispatchers.Main) { HabitEntriesAction.UpdateEntry(entry).handle(actions) }
        }
    }

    fun onEvent(event: HabitDetailsEvent) = event.handle(state)

    fun state(): StateFlow<HabitDetailsState> = state.asStateFlow()

    fun SavedStateHandle.id(): Long = this[HabitScreens.EditHabit.habitIdArg]
        ?: throw IllegalStateException("Edit Habit screen must contain habit id")
}