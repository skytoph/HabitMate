package com.github.skytoph.taski.presentation.auth.signup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.domain.auth.repository.AuthRepository
import com.github.skytoph.taski.presentation.auth.authentication.AuthResult
import com.github.skytoph.taski.presentation.auth.authentication.client.AuthResultHandler
import com.github.skytoph.taski.presentation.auth.authentication.client.GoogleAuth
import com.github.skytoph.taski.presentation.auth.signup.validation.SignUpValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val validator: SignUpValidator,
    private val state: MutableState<SignUpState> = mutableStateOf(SignUpState()),
    val client: GoogleAuth
) : SignUpEventHandler, ViewModel(), AuthResultHandler {

    fun signUp(email: String, password: String) = viewModelScope.launch {
        val authResult = repository.signUp(email, password)
        onEvent(SignUpEvent.Auth(authResult ?: return@launch))
    }

    fun validate() {
        validator.validate(state.value, this)
    }

    override fun onAuthResult(result: AuthResult) = onEvent(SignUpEvent.Auth(result))

    override fun onEvent(event: SignUpEvent) = event.handle(state)

    fun state(): State<SignUpState> = state

    fun resetState() {
        state.value = SignUpState()
    }
}

interface SignUpEventHandler {
    fun onEvent(event: SignUpEvent)
}