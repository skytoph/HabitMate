package com.github.skytoph.taski.di.auth

import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.auth.authentication.validation.EmailValidator
import com.github.skytoph.taski.presentation.auth.authentication.validation.EmptinessValidator
import com.github.skytoph.taski.presentation.auth.authentication.validation.MinLengthValidator
import com.github.skytoph.taski.presentation.auth.authentication.validation.NumericCharValidator
import com.github.skytoph.taski.presentation.auth.signin.validation.SignInValidator
import com.github.skytoph.taski.presentation.auth.signup.validation.SignUpValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ValidatorModule {

    @Provides
    fun signInValidator(): SignInValidator = SignInValidator(
        emailValidator = EmptinessValidator(
            EmailValidator(null), R.string.error_email_is_invalid
        ),
        passwordValidator = EmptinessValidator(null, R.string.error_password_is_empty),
    )

    @Provides
    fun signUpValidator(): SignUpValidator = SignUpValidator(
        emailValidator =
        EmptinessValidator(EmailValidator(null), R.string.error_email_is_invalid),
        passwordValidator =
        EmptinessValidator(
            MinLengthValidator(NumericCharValidator(null)), R.string.error_password_is_empty
        )
    )
}