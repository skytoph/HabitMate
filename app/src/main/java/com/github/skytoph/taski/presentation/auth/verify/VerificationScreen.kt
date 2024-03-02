package com.github.skytoph.taski.presentation.auth.verify

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.skytoph.taski.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    viewModel: VerificationViewModel = hiltViewModel(),
    onNavigate: () -> Unit,
    navigateUp: () -> Unit,
    buttonTimeoutInSeconds: Int = 30
) {
    var secondsLeft by remember { mutableStateOf(buttonTimeoutInSeconds) }

    LaunchedEffect(key1 = secondsLeft) {
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
        }
    }

    val isVerified = viewModel.state().value
    LaunchedEffect(key1 = isVerified) {
        if (isVerified == true) onNavigate()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.verification)) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.signOut()
                        navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    text = stringResource(R.string.verify_email_description),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text =
                    if (secondsLeft > 0) stringResource(R.string.send_email_again, secondsLeft)
                    else "",
                    minLines = 2,
                    textAlign = TextAlign.Center
                )
                OutlinedButton(
                    onClick = {
                        secondsLeft = buttonTimeoutInSeconds
                        viewModel.sendVerificationEmail()
                    },
                    enabled = secondsLeft <= 0
                ) {
                    Text(text = stringResource(R.string.send_email_button))
                }
                Button(onClick = {
                    viewModel.verify()
                }) {
                    Text(text = stringResource(R.string.verify_email_button))
                }
                if (isVerified != null && isVerified == false)
                    Text(
                        text = stringResource(R.string.error_email_is_not_verified),
                        color = MaterialTheme.colorScheme.error
                    )
            }
        })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerificationScreenPreview() {
    VerificationScreen(
        viewModel = viewModel(),
        onNavigate = {},
        navigateUp = {})
}