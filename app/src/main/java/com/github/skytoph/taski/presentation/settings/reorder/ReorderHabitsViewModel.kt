package com.github.skytoph.taski.presentation.settings.reorder

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import com.github.skytoph.taski.presentation.habit.list.view.SortHabits
import com.github.skytoph.taski.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReorderHabitsViewModel @Inject constructor(
    private val interactor: ReorderHabitsInteractor,
    private val mapper: HabitUiMapper,
    private val settings: SettingsCache,
    initAppBar: InitAppBar
) : SettingsViewModel<SettingsViewModel.Event>(settings, initAppBar) {

    private val habits: MutableState<List<HabitUi>> = mutableStateOf(emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val habitList = interactor.habits().map { habit -> mapper.map(habit) }
            withContext(Dispatchers.Main) { habits.value = habitList }
        }
    }

    fun swap(from: Int, to: Int) {
        if ((from < 0 || from > habits.value.size) || (to < 0 || to > habits.value.size)) return
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

sealed interface ReorderHabitsEvent : SettingsViewModel.Event {
    data object ApplyManualOrder : ReorderHabitsEvent {
        override suspend fun handle(settings: SettingsCache) {
            settings.updateView(sortBy = SortHabits.Manually)
        }
    }
}