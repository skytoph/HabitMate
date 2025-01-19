package com.github.skytoph.taski.presentation.habit.edit

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.core.datastore.SettingsCache
import com.github.skytoph.taski.presentation.appbar.InitAppBar
import com.github.skytoph.taski.presentation.core.EventHandler
import com.github.skytoph.taski.presentation.habit.HabitScreens
import com.github.skytoph.taski.presentation.habit.icon.IconState
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitUiMapper
import com.github.skytoph.taski.presentation.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditHabitViewModel @Inject constructor(
    private val state: MutableState<EditHabitState> = mutableStateOf(EditHabitState()),
    private val iconState: MutableState<IconState>,
    private val interactor: EditHabitInteractor,
    private val habitMapper: HabitUiMapper,
    private val validator: EditHabitValidator,
    private val savedStateHandle: SavedStateHandle,
    settings: SettingsCache,
    initAppBar: InitAppBar
) : SettingsViewModel<SettingsViewModel.Event>(settings, initAppBar), EventHandler<EditHabitEvent> {

    fun init(reminderAllowed: Boolean) {
        onEvent(EditHabitEvent.Progress(true))
        viewModelScope.launch(Dispatchers.IO) {
            val habit = habitMapper.map(interactor.habit(savedStateHandle.id()))
            withContext(Dispatchers.Main) { onEvent(EditHabitEvent.Init(habit, reminderAllowed)) }
        }
    }

    fun saveHabit(navigateUp: () -> Unit, context: Context) =
        viewModelScope.launch(Dispatchers.IO) {
            val habit = state.value.toHabitUi()
            val isFirstDaySunday = settings().value.weekStartsOnSunday.value
            interactor.insert(habit, context, isFirstDaySunday)
            withContext(Dispatchers.Main) { navigateUp() }
        }

    fun validate() = validator.validate(state.value.title, this)

    override fun onEvent(event: EditHabitEvent) = event.handle(state, iconState)

    fun state(): State<EditHabitState> = state

    fun iconState(): State<IconState> = iconState

    fun SavedStateHandle.id(): Long = this[HabitScreens.EditHabit.habitIdArg]
        ?: throw IllegalStateException("Edit Habit screen must contain habit id")
}