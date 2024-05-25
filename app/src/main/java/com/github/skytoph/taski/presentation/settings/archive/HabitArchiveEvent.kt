package com.github.skytoph.taski.presentation.settings.archive

import androidx.compose.runtime.MutableState
import com.github.skytoph.taski.presentation.habit.HabitUi

sealed interface HabitArchiveEvent {
    fun handle(state: MutableState<ArchiveState>)

    class Update(private val habits: List<HabitUi>) : HabitArchiveEvent {
        override fun handle(state: MutableState<ArchiveState>) {
            state.value = state.value.copy(habits = habits)
        }
    }

    class UpdateDeleteDialog(private val id: Long? = null) : HabitArchiveEvent {
        override fun handle(state: MutableState<ArchiveState>) {
            state.value = state.value.copy(deleteHabitById = id)
        }
    }
}
