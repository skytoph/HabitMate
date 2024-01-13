package com.github.skytoph.taski.presentation.habit.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditHabitViewModel @Inject constructor(
    private val state: MutableState<EditHabitState> = mutableStateOf(EditHabitState()),
    private val repository: HabitRepository,
    private val mapper: HabitDomainMapper
) : ViewModel(), SelectIcon {

    fun saveHabit() = viewModelScope.launch(Dispatchers.IO) {
        val habit = state.value.toHabitUi().map(mapper)
        repository.insert(habit)
    }

    fun onEvent(event: EditHabitEvent) = event.handle(state)

    override fun selectIcon(icon: ImageVector?, color: Color?) =
        onEvent(EditHabitEvent.UpdateIcon(icon, color))

    override fun state(): State<EditHabitState> = state
}