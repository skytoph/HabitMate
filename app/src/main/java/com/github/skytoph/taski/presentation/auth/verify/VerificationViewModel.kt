package com.github.skytoph.taski.presentation.auth.verify

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.domain.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val state: MutableState<Boolean?> = mutableStateOf(null)
) : ViewModel() {

    init {
        sendVerificationEmail()
    }

    fun sendVerificationEmail() = viewModelScope.launch {
        repository.sendVerificationEmail()
        withContext(Dispatchers.Main) { state.value = null }
    }

    fun verify() = viewModelScope.launch(Dispatchers.IO) {
        repository.reloadUser(onVerified = { isVerified -> state.value = isVerified })
    }

    fun signOut() = viewModelScope.launch {
        repository.signOut()
    }

    fun state(): State<Boolean?> = state
}