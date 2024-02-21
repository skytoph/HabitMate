package com.github.skytoph.taski.presentation.habit.edit

import kotlinx.coroutines.flow.MutableStateFlow

class UpdateEntryAction(val entry: EntryEditableUi) {
    fun handle(state: MutableStateFlow<List<UpdateEntryAction>>) {
        state.value += this
    }
}