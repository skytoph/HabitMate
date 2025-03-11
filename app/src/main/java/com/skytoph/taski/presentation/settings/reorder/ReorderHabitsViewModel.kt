package com.skytoph.taski.presentation.settings.reorder

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.core.datastore.settings.SortHabits
import com.skytoph.taski.presentation.appbar.InitAppBar
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import com.skytoph.taski.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReorderHabitsViewModel @Inject constructor(
    private val interactor: ReorderHabitsInteractor,
    private val mapper: HabitUiMapper,
    private val settings: SettingsCache,
    private val state: MutableState<ReorderState>,
    initAppBar: InitAppBar
) : SettingsViewModel<SettingsViewModel.Event>(settings, initAppBar) {

    private val habits: MutableState<List<HabitUi>> = mutableStateOf(emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val habitList = interactor.habits().map { habit -> mapper.map(habit) }
            withContext(Dispatchers.Main) { habits.value = habitList }
            settings().map { it.view.sortBy }
                .distinctUntilChanged()
                .collect { sort -> onEvent(ReorderHabitsEvent.UpdateReminder(!SortHabits.Manually.matches(sort))) }
        }
    }

    fun state(): State<ReorderState> = state

    fun onEvent(event: ReorderHabitsEvent) = event.handle(state)

    fun swap(from: Int, to: Int) {
        if (from < 0 || from >= habits.value.size || to < 0 || to >= habits.value.size) return
        habits.value = habits.value.toMutableList().apply { add(to, removeAt(from)) }
    }

    fun saveOrder(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        interactor.update(habits.value, context)
    }

    fun habits(): State<List<HabitUi>> = habits

    fun applyManualOrder() = viewModelScope.launch(Dispatchers.IO) {
        ReorderHabitsEvent.ApplyManualOrder.handle(settings)
    }
}