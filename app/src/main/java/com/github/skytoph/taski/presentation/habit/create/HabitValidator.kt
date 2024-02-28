package com.github.skytoph.taski.presentation.habit.create

import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.auth.authentication.validation.EmptinessValidator
import com.github.skytoph.taski.presentation.auth.authentication.validation.Validator
import com.github.skytoph.taski.presentation.core.EventHandler
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.core.state.StringResource

abstract class HabitValidator<Event>(
    private val validator: Validator = EmptinessValidator(null, R.string.error_habit_title_is_empty)
) {

    fun validate(state: FieldState, eventHandler: EventHandler<Event>) {
        val result = validator.validate(state.field).errorResId?.let { StringResource.ResId(it) }
        eventHandler.onEvent(titleErrorEvent(result))

        if (result == null) eventHandler.onEvent(validateEvent())
    }

    abstract fun titleErrorEvent(result: StringResource?): Event
    abstract fun validateEvent(): Event
}