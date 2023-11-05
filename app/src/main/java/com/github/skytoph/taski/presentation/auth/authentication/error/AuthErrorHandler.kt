package com.github.skytoph.taski.presentation.auth.authentication.error

import android.util.Log
import com.github.skytoph.taski.presentation.auth.authentication.AuthResult
import com.github.skytoph.taski.presentation.auth.mapper.AuthErrorMapper
import java.util.concurrent.CancellationException

class AuthErrorHandler(
    private val mapper: AuthErrorMapper,
) {

    fun handle(exception: Exception?): AuthResult {
        if (exception is CancellationException) throw exception
        Log.e(this::class.java.simpleName, exception?.stackTraceToString().toString())
        return AuthResult(user = null, error = mapper.map(exception))
    }
}