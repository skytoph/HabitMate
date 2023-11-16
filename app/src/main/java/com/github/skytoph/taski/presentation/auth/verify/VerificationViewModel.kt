package com.github.skytoph.taski.presentation.auth.verify

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.domain.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val state: MutableState<Boolean?> = mutableStateOf(null)
) : ViewModel() {

    fun sendVerificationEmail() = viewModelScope.launch {
        repository.sendVerificationEmail()
    }

    fun verify() = viewModelScope.launch {
        repository.reloadUser()
        val verified = repository.currentUser()?.isVerified ?: false
        state.value = verified
    }

    fun signOut() = viewModelScope.launch {
        repository.signOut()
    }

    fun state(): State<Boolean?> = state
}