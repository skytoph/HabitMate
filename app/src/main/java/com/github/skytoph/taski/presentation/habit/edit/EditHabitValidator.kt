package com.github.skytoph.taski.presentation.habit.edit

import com.github.skytoph.taski.presentation.core.state.StringResource
import com.github.skytoph.taski.presentation.habit.create.HabitValidator

class EditHabitValidator : HabitValidator<EditHabitEvent>() {
    override fun titleErrorEvent(result: StringResource?): EditHabitEvent =
        EditHabitEvent.TitleError(result)

    override fun validateEvent(): EditHabitEvent = EditHabitEvent.Validate
}