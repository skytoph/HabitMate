package com.github.skytoph.taski.presentation.auth.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.auth.authentication.SignInWithGoogle
import com.github.skytoph.taski.presentation.auth.authentication.TextDivider
import com.github.skytoph.taski.presentation.auth.authentication.client.makeLauncher
import com.github.skytoph.taski.presentation.auth.authentication.client.signInWithGoogle
import com.github.skytoph.taski.presentation.auth.signup.mapper.map
import com.github.skytoph.taski.presentation.core.component.BasicTextField
import com.github.skytoph.taski.presentation.core.component.ErrorText
import com.github.skytoph.taski.presentation.core.component.PasswordField

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigate: () -> Unit
) {
    val state = viewModel.state()
    val context = LocalContext.current
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    val launcher = makeLauncher(lifecycleScope, viewModel.client, viewModel)

    LaunchedEffect(key1 = state.value.isValid) {
        if (state.value.isValid)
            viewModel.signUp(state.value.email.field, state.value.password.field)
    }
    LaunchedEffect(key1 = state.value.auth.signInError) {
        if (state.value.isValid)
            viewModel.onEvent(state.value.auth.signInError?.map() ?: return@LaunchedEffect)
    }
    LaunchedEffect(key1 = state.value.auth.isSignInSuccessful) {
        if (state.value.auth.isSignInSuccessful) {
            viewModel.resetState()
            onNavigate()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ErrorText(error = state.value.error?.getString(context))
        BasicTextField(
            state.value.email.field,
            error = state.value.email.error?.getString(context),
            onValueChange = { viewModel.onEvent(SignUpEvent.TypeEmail(it)) })
        PasswordField(
            value = state.value.password.field,
            onValueChange = { viewModel.onEvent(SignUpEvent.TypePassword(it)) },
            label = "password",
            isVisible = state.value.isPasswordVisible,
            error = state.value.password.error?.getString(context),
            imeAction = ImeAction.Next,
            onVisibleClick = { viewModel.onEvent(SignUpEvent.ChangeVisibility) }
        )
        PasswordField(
            value = state.value.passwordConfirmation.field,
            onValueChange = { viewModel.onEvent(SignUpEvent.TypePasswordConfirmation(it)) },
            label = "confirm the password",
            isVisible = state.value.isPasswordVisible,
            error = state.value.passwordConfirmation.error?.getString(context),
            imeAction = ImeAction.Done,
            onVisibleClick = { viewModel.onEvent(SignUpEvent.ChangeVisibility) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.validate()
            },
        ) {
            Text(text = "Sign up")
        }

        TextDivider(stringResource(id = R.string.sign_in_with))
        SignInWithGoogle(onClick = {
            signInWithGoogle(lifecycleScope, viewModel.client, launcher, viewModel)
        })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(viewModel(), {})
}