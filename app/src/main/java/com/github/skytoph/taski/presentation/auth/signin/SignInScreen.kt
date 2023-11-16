package com.github.skytoph.taski.presentation.auth.signin

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
import com.github.skytoph.taski.presentation.auth.signin.mapper.map
import com.github.skytoph.taski.ui.components.BasicTextField
import com.github.skytoph.taski.ui.components.ErrorText
import com.github.skytoph.taski.ui.components.PasswordField

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigate: () -> Unit,
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    val state = viewModel.state()
    val launcher = makeLauncher(lifecycleScope, viewModel.client, viewModel)

    LaunchedEffect(key1 = state.value.isValid) {
        if (state.value.isValid)
            viewModel.signIn(state.value.email.field, state.value.password.field)
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
        ErrorText(error = state.value.error?.let { it.getString(LocalContext.current) })
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = state.value.email.field,
            error = state.value.email.error?.getString(LocalContext.current),
            onValueChange = { viewModel.onEvent(SignInEvent.TypeEmail(it)) })
        PasswordField(
            value = state.value.password.field,
            onValueChange = { viewModel.onEvent(SignInEvent.TypePassword(it)) },
            label = stringResource(R.string.password),
            isVisible = state.value.isPasswordVisible,
            error = state.value.password.error?.getString(LocalContext.current),
            imeAction = ImeAction.Done,
        ) { viewModel.onEvent(SignInEvent.ChangeVisibility) }
        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                viewModel.validate()
            },
        ) {
            Text(text = stringResource(id = R.string.sign_in))
        }
        TextDivider(stringResource(id = R.string.sign_in_with))
        SignInWithGoogle(onClick = {
            signInWithGoogle(lifecycleScope, viewModel.client, launcher, viewModel)
        })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    SignInScreen(
        viewModel(),
        {},
    )
}