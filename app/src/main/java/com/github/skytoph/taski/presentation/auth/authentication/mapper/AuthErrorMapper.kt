package com.github.skytoph.taski.presentation.auth.authentication.mapper

import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.auth.authentication.error.AuthError
import com.github.skytoph.taski.ui.state.StringResource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class AuthErrorMapper {

    fun map(exception: Exception?): AuthError {
        return if (exception is FirebaseAuthException)
            when {
                exception is FirebaseAuthEmailException || exception.errorCode.contains("EMAIL") ->
                    AuthError.EmailError(StringResource.Value(exception.localizedMessage))

                exception is FirebaseAuthWeakPasswordException || exception.errorCode.contains("PASSWORD") ->
                    AuthError.PasswordError(StringResource.Value(exception.localizedMessage))

                else ->
                    AuthError.GeneralError(StringResource.Value(exception.localizedMessage))
            }
        else if (exception is FirebaseException) {
            when {
                exception.message?.contains("INVALID_LOGIN_CREDENTIALS") ?: false ->
                    AuthError.GeneralError(StringResource.ResId(R.string.error_incorrect_email_address_or_password))

                else -> AuthError.GeneralError(StringResource.ResId(R.string.error_auth_general))
            }
        } else AuthError.GeneralError(StringResource.ResId(R.string.error_auth_general))
    }
}