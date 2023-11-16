package com.github.skytoph.taski.presentation.auth.signin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.domain.auth.repository.AuthRepository
import com.github.skytoph.taski.presentation.auth.authentication.AuthResult
import com.github.skytoph.taski.presentation.auth.authentication.client.AuthResultHandler
import com.github.skytoph.taski.presentation.auth.authentication.client.GoogleAuth
import com.github.skytoph.taski.presentation.auth.signin.validation.SignInValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val validator: SignInValidator,
    private val state: MutableState<SignInState> = mutableStateOf(SignInState()),
    val client: GoogleAuth
) : SignInEventHandler, AuthResultHandler, ViewModel() {

    fun signIn(email: String, password: String) = viewModelScope.launch {
        val authResult = repository.signIn(email, password)
        onAuthResult(authResult ?: return@launch)
    }

    fun validate() = validator.validate(state.value, this)

    override fun onAuthResult(result: AuthResult) = onEvent(SignInEvent.Auth(result))

    override fun onEvent(event: SignInEvent) = event.handle(state)

    fun state(): State<SignInState> = state

    fun resetState() {
        state.value = SignInState()
    }
}

interface SignInEventHandler {
    fun onEvent(event: SignInEvent)
}