package com.github.skytoph.taski.presentation.auth.signin.component

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
import com.github.skytoph.taski.presentation.auth.signin.SignInEvent
import com.github.skytoph.taski.presentation.auth.signin.SignInState
import com.github.skytoph.taski.presentation.auth.signin.SignInViewModel
import com.github.skytoph.taski.presentation.auth.signin.mapper.map
import com.github.skytoph.taski.presentation.core.component.BasicTextField
import com.github.skytoph.taski.presentation.core.component.ErrorText
import com.github.skytoph.taski.presentation.core.component.PasswordField
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigate: () -> Unit,
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    val launcher = makeLauncher(lifecycleScope, viewModel.client, viewModel)
    val state = viewModel.state()

    SignIn(
        state = state,
        onNavigate = { viewModel.resetState(); onNavigate() },
        onSignInWithGoogle = {
            signInWithGoogle(lifecycleScope, viewModel.client, launcher, viewModel)
        },
        signIn = { viewModel.signIn(state.value.email.field, state.value.password.field) },
        handleError = { viewModel.onEvent(state.value.auth.signInError?.map() ?: return@SignIn) },
        onEditEmail = { viewModel.onEvent(SignInEvent.TypeEmail(it)) },
        onEditPassword = { viewModel.onEvent(SignInEvent.TypePassword(it)) },
        onPasswordVisibleClick = { viewModel.onEvent(SignInEvent.ChangeVisibility) },
        onValidate = { viewModel.validate() }
    )
}

@Composable
private fun SignIn(
    state: State<SignInState>,
    onNavigate: () -> Unit = {},
    onSignInWithGoogle: () -> Unit = {},
    signIn: () -> Unit = {},
    handleError: () -> Unit = {},
    onEditEmail: (String) -> Unit = {},
    onEditPassword: (String) -> Unit = {},
    onPasswordVisibleClick: () -> Unit = {},
    onValidate: () -> Unit = {}
) {

    LaunchedEffect(key1 = state.value.isValid) {
        if (state.value.isValid) signIn()
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
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ErrorText(error = state.value.error?.let { it.getString(LocalContext.current) })
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = state.value.email.field,
                error = state.value.email.error?.getString(LocalContext.current),
                onValueChange = onEditEmail
            )
            PasswordField(
                value = state.value.password.field,
                onValueChange = onEditPassword,
                label = stringResource(R.string.password),
                isVisible = state.value.isPasswordVisible,
                error = state.value.password.error?.getString(LocalContext.current),
                imeAction = ImeAction.Done,
                onVisibleClick = onPasswordVisibleClick,
            )
            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = onValidate,
            ) {
                Text(text = stringResource(id = R.string.sign_in))
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
fun SignInScreenPreview() {
    HabitMateTheme {
        SignIn(remember { mutableStateOf(SignInState()) })
    }
}