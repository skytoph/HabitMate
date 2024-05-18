package com.github.skytoph.taski.presentation.auth.signup.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.auth.authentication.SignInWithGoogle
import com.github.skytoph.taski.presentation.auth.authentication.TextDivider
import com.github.skytoph.taski.presentation.auth.authentication.client.makeLauncher
import com.github.skytoph.taski.presentation.auth.authentication.client.signInWithGoogle
import com.github.skytoph.taski.presentation.auth.signup.SignUpEvent
import com.github.skytoph.taski.presentation.auth.signup.SignUpState
import com.github.skytoph.taski.presentation.auth.signup.SignUpViewModel
import com.github.skytoph.taski.presentation.auth.signup.mapper.map
import com.github.skytoph.taski.presentation.core.component.BasicAuthTextField
import com.github.skytoph.taski.presentation.core.component.ErrorText
import com.github.skytoph.taski.presentation.core.component.PasswordField
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigate: () -> Unit
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    val launcher = makeLauncher(lifecycleScope, viewModel.client, viewModel)

    val state = viewModel.state()

    SignIn(
        state = state,
        onNavigate = { viewModel.resetState();onNavigate() },
        onSignInWithGoogle = {
            signInWithGoogle(lifecycleScope, viewModel.client, launcher, viewModel)
        },
        onValidateState = { viewModel.validate() },
        signUp = { viewModel.signUp(state.value.email.field, state.value.password.field) },
        handleError = { viewModel.onEvent(state.value.auth.signInError?.map() ?: return@SignIn) },
        onEditEmail = { viewModel.onEvent(SignUpEvent.TypeEmail(it)) },
        onEditPassword = { viewModel.onEvent(SignUpEvent.TypePassword(it)) },
        onPasswordVisibleClick = { viewModel.onEvent(SignUpEvent.ChangeVisibility) },
        onEditPasswordConfirmation = { viewModel.onEvent(SignUpEvent.TypePasswordConfirmation(it)) })
}

@Composable
private fun SignIn(
    state: State<SignUpState>,
    onNavigate: () -> Unit = {},
    onSignInWithGoogle: () -> Unit = {},
    onValidateState: () -> Unit = {},
    signUp: () -> Unit = {},
    handleError: () -> Unit = {},
    onEditEmail: (String) -> Unit = {},
    onEditPassword: (String) -> Unit = {},
    onPasswordVisibleClick: () -> Unit = {},
    onEditPasswordConfirmation: (String) -> Unit = {},
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.value.isValid) {
        if (state.value.isValid) signUp()
    }
    LaunchedEffect(key1 = state.value.auth.signInError) {
        if (state.value.isValid) handleError()
    }
    LaunchedEffect(key1 = state.value.auth.isSignInSuccessful) {
        if (state.value.auth.isSignInSuccessful) onNavigate()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BasicAuthTextField(
                state.value.email.field,
                error = state.value.email.error?.getString(context),
                onValueChange = onEditEmail
            )
            PasswordField(
                value = state.value.password.field,
                onValueChange = onEditPassword,
                label = "password",
                isVisible = state.value.isPasswordVisible,
                error = state.value.password.error?.getString(context),
                imeAction = ImeAction.Next,
                onVisibleClick = onPasswordVisibleClick
            )
            PasswordField(
                value = state.value.passwordConfirmation.field,
                onValueChange = onEditPasswordConfirmation,
                label = "confirm the password",
                isVisible = state.value.isPasswordVisible,
                error = state.value.passwordConfirmation.error?.getString(context),
                imeAction = ImeAction.Done,
                onVisibleClick = onPasswordVisibleClick
            )
            ErrorText(error = state.value.error?.getString(context))
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onValidateState) {
                Text(text = "Sign up")
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextDivider(stringResource(id = R.string.sign_in_with))
            SignInWithGoogle(onClick = onSignInWithGoogle)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    HabitMateTheme {
        SignIn(remember { mutableStateOf(SignUpState()) })
    }
}