package com.skytoph.taski.presentation.habit.edit

import com.skytoph.taski.presentation.core.state.StringResource
import com.skytoph.taski.presentation.habit.create.HabitValidator

class EditHabitValidator : HabitValidator<EditHabitEvent>() {
    override fun titleErrorEvent(result: StringResource?): EditHabitEvent =
        EditHabitEvent.TitleError(result)

    override fun validateEvent(): EditHabitEvent = EditHabitEvent.Validate
}