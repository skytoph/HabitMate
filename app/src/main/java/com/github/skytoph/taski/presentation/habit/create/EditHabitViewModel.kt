package com.github.skytoph.taski.presentation.habit.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel

class EditHabitViewModel(
    private val state: MutableState<EditHabitState> = mutableStateOf(EditHabitState())
) : ViewModel(), SelectIcon {

    fun saveHabit() {

    }

    fun onEvent(event: EditHabitEvent) = event.handle(state)

    override fun selectIcon(icon: ImageVector?, color: Color?) =
        onEvent(EditHabitEvent.UpdateIcon(icon, color))

    override fun state(): State<EditHabitState> = state
}