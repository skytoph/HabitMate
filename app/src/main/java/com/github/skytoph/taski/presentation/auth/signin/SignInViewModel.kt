package com.github.skytoph.taski.presentation.auth.signin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.github.skytoph.taski.presentation.auth.signin.validation.SignInValidator

class SignInViewModel : ViewModel(), SignInEventHandler {

    private val validator: SignInValidator = SignInValidator()

    private var state: MutableState<SignInState> = mutableStateOf(SignInState())

    fun validate() {
        validator.validate(state.value, this)
    }

    override fun onEvent(event: SignInEvent) {
        event.handle(state)
    }

    fun state(): State<SignInState> = state
}

interface SignInEventHandler {
    fun onEvent(event: SignInEvent)
}

