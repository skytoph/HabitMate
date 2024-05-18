package com.github.skytoph.taski.presentation.habit.reorder

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.core.component.AppBarState
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReorderHabitsViewModel @Inject constructor(
    private val interactor: ReorderHabitsInteractor,
    private val mapper: HabitUiMapper,
    state: MutableState<AppBarState>,
) : ViewModel(), InitAppBar by InitAppBar.Base(state) {

    private val habits: MutableState<List<HabitUi>> = mutableStateOf(emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val habitList = interactor.habits().map { habit -> mapper.map(habit) }
            withContext(Dispatchers.Main) { habits.value = habitList }
        }
    }

    fun swap(from: Int, to: Int) {
        habits.value = habits.value.toMutableList().apply { add(to, removeAt(from)) }
    }

    fun saveOrder(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        interactor.update(habits.value, context)
    }

    fun habits(): State<List<HabitUi>> = habits
}