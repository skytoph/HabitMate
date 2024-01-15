package com.github.skytoph.taski.presentation.habit.icon

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.github.skytoph.taski.presentation.habit.create.EditHabitEvent
import com.github.skytoph.taski.presentation.habit.create.EditHabitState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectIconViewModel @Inject constructor(
    private val state: MutableState<EditHabitState>
) : ViewModel() {

    fun selectIcon(icon: ImageVector? = null, color: Color? = null) =
        EditHabitEvent.UpdateIcon(icon, color).handle(state)

    fun state(): State<EditHabitState> = state
}