package com.github.skytoph.taski.presentation.auth.signup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.github.skytoph.taski.presentation.auth.signup.validation.SignUpValidator

class SignUpViewModel : ViewModel(), SignUpEventHandler {

    private val validator: SignUpValidator = SignUpValidator()
    private val state = mutableStateOf(SignUpState())

    override fun onEvent(event: SignUpEvent) = event.handle(state)

    fun state(): State<SignUpState> = state

    fun validate() {
        validator.validate(state.value, this)
    }

    fun resetState() {
        state.value = SignUpState()
    }
}

interface SignUpEventHandler {
    fun onEvent(event: SignUpEvent)
}