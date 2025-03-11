package com.skytoph.taski.presentation.habit.create

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.presentation.appbar.InitAppBar
import com.skytoph.taski.presentation.core.EventHandler
import com.skytoph.taski.presentation.habit.icon.IconState
import com.skytoph.taski.presentation.habit.icon.SelectIconEvent
import com.skytoph.taski.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateHabitViewModel @Inject constructor(
    private val state: MutableState<CreateHabitState> = mutableStateOf(CreateHabitState()),
    private val iconState: MutableState<IconState>,
    private val validator: HabitValidator<CreateHabitEvent>,
    private val interactor: CreateHabitInteractor,
    settings: SettingsCache,
    initAppBar: InitAppBar
) : SettingsViewModel<SettingsViewModel.Event>(settings, initAppBar), EventHandler<CreateHabitEvent> {

    init {
        SelectIconEvent.Clear.handle(iconState)
    }

    fun saveHabit(onNavigate: () -> Unit, context: Context) =
        viewModelScope.launch(Dispatchers.IO) {
            val habit = state.value.toHabitUi()
            val isFirstDaySunday = settings().value.weekStartsOnSunday.value
            interactor.insert(habit, context, isFirstDaySunday)
            withContext(Dispatchers.Main) { onNavigate() }
        }

    fun validate() = validator.validate(state.value.title, this)

    override fun onEvent(event: CreateHabitEvent) = event.handle(state)

    fun state(): State<CreateHabitState> = state

    fun iconState(): State<IconState> = iconState
}