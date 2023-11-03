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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.skytoph.taski.ui.components.BasicTextField
import com.github.skytoph.taski.ui.components.ErrorText
import com.github.skytoph.taski.ui.components.PasswordField

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onSignUp: () -> Unit
) {
    val state = viewModel.state()

    LaunchedEffect(key1 = state.value.isValid) {
        if (state.value.isValid) onSignUp()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ErrorText(error = state.value.error?.let { stringResource(it) })
        BasicTextField(
            state.value.email.field,
            errorResId = state.value.email.errorResId,
            onValueChange = { viewModel.onEvent(SignUpEvent.TypeEmail(it)) })
        PasswordField(
            value = state.value.password.field,
            onValueChange = { viewModel.onEvent(SignUpEvent.TypePassword(it)) },
            label = "password",
            isVisible = state.value.isPasswordVisible,
            errorResId = state.value.password.errorResId,
            onVisibleClick = { viewModel.onEvent(SignUpEvent.ChangeVisibility) }
        )
        PasswordField(
            value = state.value.passwordConfirmation.field,
            onValueChange = { viewModel.onEvent(SignUpEvent.TypePasswordConfirmation(it)) },
            label = "confirm the password",
            isVisible = state.value.isPasswordVisible,
            errorResId = state.value.passwordConfirmation.errorResId,
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
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(viewModel()) {}
}