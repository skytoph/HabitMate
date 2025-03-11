package com.skytoph.taski.presentation.habit.create

import com.skytoph.taski.presentation.core.state.StringResource

class NewHabitValidator : HabitValidator<CreateHabitEvent>() {
    override fun titleErrorEvent(result: StringResource?): CreateHabitEvent =
        CreateHabitEvent.TitleError(result)

    override fun validateEvent(): CreateHabitEvent = CreateHabitEvent.Validate
}