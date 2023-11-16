package com.github.skytoph.taski.presentation.auth.authentication.client

import com.github.skytoph.taski.presentation.auth.authentication.AuthResult

interface AuthResultHandler {
    fun onAuthResult(result: AuthResult)
}