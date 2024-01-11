package com.github.skytoph.taski.presentation.auth.authentication.client

import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleCoroutineScope
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.auth.authentication.AuthResult
import com.github.skytoph.taski.presentation.auth.authentication.error.AuthError
import com.github.skytoph.taski.presentation.core.state.StringResource
import kotlinx.coroutines.launch

fun signInWithGoogle(
    lifecycleScope: LifecycleCoroutineScope,
    client: GoogleAuth,
    launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    resultHandler: AuthResultHandler
) {
    lifecycleScope.launch {
        val signInIntentSender = client.signInWithGoogle()
        if (signInIntentSender == null) {
            val error = AuthError.GeneralError(StringResource.ResId(R.string.error_auth_general))
            resultHandler.onAuthResult(AuthResult(error = error))
        } else launcher.launch(IntentSenderRequest.Builder(signInIntentSender).build())
    }
}

@Composable
fun makeLauncher(
    lifecycleScope: LifecycleCoroutineScope,
    client: GoogleAuth,
    resultHandler: AuthResultHandler
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartIntentSenderForResult(),
    onResult = { result ->
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            lifecycleScope.launch {
                result.data?.let { intent ->
                    val signInResult = client.signInWithIntent(intent = intent)
                    resultHandler.onAuthResult(signInResult)
                }
            }
        }
    }
)