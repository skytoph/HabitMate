package com.skytoph.taski.presentation.habit.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.domain.habit.HabitWithEntries
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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
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
        interactor.entriesEditable(savedStateHandle.id())
            .cachedIn(viewModelScope)
            .combine(actions) { pagingData, actions ->
                actions.fold(pagingData) { paging, event -> paging.map { data -> event.apply(data) } }
            }

    val entriesBarChart: Flow<PagingData<BarItemUi>> =
        interactor.entriesBarChart(savedStateHandle.id())
            .cachedIn(viewModelScope)
            .combine(actions) { pagingData, actions ->
                actions.fold(pagingData) { paging, event -> paging.map { data -> event.apply(data) } }
            }

    private val history: SharedFlow<HabitWithEntries> =
        interactor.entries(savedStateHandle.id())
            .filterNotNull()
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    init {
        interactor.habit(savedStateHandle.id())
            .map { habit -> habit?.let { habitMapper.map(it) } }
            .flowOn(Dispatchers.IO)
            .onEach { it?.let { habit -> onEvent(HabitDetailsEvent.Init(habit)) } }
            .launchIn(viewModelScope)

        history
            .map { history ->
                statsMapper.statistics(history, settings().value.weekStartsOnSunday.value)
            }.flowOn(Dispatchers.IO)
            .onEach { stats ->
                onEvent(HabitDetailsEvent.UpdateStats(stats))
                if (settings().value.streaksHighlighted)
                    HabitEntriesAction.ApplyStatistics(stats).handle(actions)
            }.launchIn(viewModelScope)

        settings.state()
            .distinctUntilChangedBy { it.habitDetailsView?.barChartFrequency }
            .combine(history) { _, history ->
                onEvent(HabitDetailsEvent.UpdateBarChart(maxValue = interactor.findMaxBarChartEntry(history.entries)))
            }.flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun habitDone(daysAgo: Int) = state.value.habit?.let { habit ->
        viewModelScope.launch(Dispatchers.IO) {
            val updated = interactor.habitDoneAndReturn(habit, daysAgo)
            HabitEntriesAction.UpdateEntry(updated).handle(actions)
        }
    }

    fun onEvent(event: HabitDetailsEvent) = event.handle(state)

    fun state(): StateFlow<HabitDetailsState> = state.asStateFlow()

    fun SavedStateHandle.id(): Long = this[HabitScreens.EditHabit.habitIdArg]
        ?: throw IllegalStateException("Habit screen must contain habit id")
}