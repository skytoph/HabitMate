package com.github.skytoph.taski.presentation.habit.details

import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import kotlinx.coroutines.flow.MutableStateFlow

class UpdateEntryAction(val entry: EntryEditableUi) {
    fun handle(state: MutableStateFlow<List<UpdateEntryAction>>) {
        state.value += this
    }
}