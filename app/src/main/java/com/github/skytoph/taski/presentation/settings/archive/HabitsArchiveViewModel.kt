package com.github.skytoph.taski.presentation.settings.archive

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.core.component.AppBarState
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsArchiveViewModel @Inject constructor(
    private val interactor: UnarchiveHabitsInteractor,
    private val mapper: HabitUiMapper,
    private val state: MutableState<ArchiveState>,
    appBarState: MutableState<AppBarState>,
) : ViewModel(), InitAppBar by InitAppBar.Base(appBarState) {

    init {
        interactor.habitsFlow()
            .map { habits -> habits.map { habit -> mapper.map(habit) } }
            .flowOn(Dispatchers.IO)
            .onEach { habits -> onEvent(HabitArchiveEvent.Update(habits)) }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: HabitArchiveEvent) = event.handle(state)

    fun restore(id: Long, message: String) = viewModelScope.launch(Dispatchers.IO) {
        interactor.archive(id, message, false)
    }

    fun delete(id: Long, message: String, context: Context) =
        viewModelScope.launch(Dispatchers.IO) {
            interactor.delete(id, message, context)
        }

    fun state(): State<ArchiveState> = state
}
