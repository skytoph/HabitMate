package com.github.skytoph.taski.presentation.auth.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun AuthenticationScreen(
    onSignInClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SignInButton(onSignInClick)
        SignUpButton(onSignUpClick)
    }
}

@Composable
fun SignInButton(onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = stringResource(id = R.string.sign_in))
    }
}

@Composable
fun SignUpButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = stringResource(id = R.string.sign_up))
    }
}

@Composable
fun SignInWithGoogle(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.google_light_rd_na),
            tint = Color.Unspecified,
            contentDescription = stringResource(id = R.string.sign_in_with_google)
        )
    }
}

@Composable
fun TextDivider(label: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Divider(modifier = Modifier.weight(1f), thickness = 1.dp)
        Text(text = label, Modifier.padding(8.dp), style = MaterialTheme.typography.labelSmall)
        Divider(modifier = Modifier.weight(1f), thickness = 1.dp)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AuthenticationScreenPreview() {
    HabitMateTheme {
        AuthenticationScreen()
    }
}
