package com.github.skytoph.taski.presentation

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.skytoph.taski.presentation.auth.authentication.AuthViewModel
import com.github.skytoph.taski.presentation.auth.authentication.AuthenticationScreen
import com.github.skytoph.taski.presentation.auth.authentication.client.GoogleAuthUiClient
import com.github.skytoph.taski.presentation.auth.authentication.user.UserData
import com.github.skytoph.taski.presentation.auth.signin.SignInScreen
import com.github.skytoph.taski.presentation.auth.signup.SignUpScreen
import com.github.skytoph.taski.presentation.auth.verify.VerificationScreen
import com.github.skytoph.taski.presentation.profile.ProfileScreen
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

private enum class Screens {
    Authentication,
    SignIn,
    SignUp,
    Verify,
    Profile,
}

@Composable
fun TaskiApp(navController: NavHostController = rememberNavController()) {
    val applicationContext = LocalContext.current.applicationContext
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    val viewModel: AuthViewModel = viewModel()
    val authState by viewModel.state().collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val start = googleAuthUiClient.getSignedInUser().getStartDestination()
        NavHost(navController = navController, startDestination = start) {
            composable(route = Screens.Authentication.name) {
                val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == ComponentActivity.RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onAuthResult(signInResult)
                            }
                        }
                    }
                )

                AuthenticationScreen(
                    onSignInClick = { navController.navigate(Screens.SignIn.name) },
                    onSignUpClick = { navController.navigate(Screens.SignUp.name) },
                    onSignInWithGoogleClick = {
                        lifecycleScope.launch {
                            val signInIntentSender = googleAuthUiClient.signInWithGoogle()
                            if (signInIntentSender == null) // TODO: fix, remove toast
                                Toast.makeText(applicationContext, "error", Toast.LENGTH_LONG)
                                    .show()
                            else launcher.launch(
                                IntentSenderRequest.Builder(signInIntentSender).build()
                            )
                        }
                    },
                )
            }
            composable(route = Screens.SignIn.name) {
                val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
                SignInScreen(
                    viewModel = viewModel(),
                    authState = authState,
                    onSignIn = { email, password ->
                        lifecycleScope.launch {
                            val signInResult = googleAuthUiClient.signIn(email, password)
                            viewModel.onAuthResult(signInResult)
                        }
                    },
                    onNavigate = {
                        navController.navigateToProfile(googleAuthUiClient.getSignedInUser()?.isVerified)
                    })
            }
            composable(route = Screens.SignUp.name) {
                val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
                SignUpScreen(
                    viewModel = viewModel(),
                    authState = authState,
                    onSignUp = { email, password ->
                        lifecycleScope.launch {
                            val signUpResult = googleAuthUiClient.signUp(email, password)
                            viewModel.onAuthResult(signUpResult)
                        }
                    },
                    onNavigate = {
                        navController.navigateToProfile(googleAuthUiClient.getSignedInUser()?.isVerified)
                    })
            }
            composable(route = Screens.Profile.name) {
                val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
                ProfileScreen() {
                    lifecycleScope.launch {
                        googleAuthUiClient.signOut()
                        navController.navigateAndClear(Screens.Authentication.name)
                    }
                }
            }
            composable(route = Screens.Verify.name) {
                VerificationScreen()
            }
        }
    }
}

private fun NavHostController.navigateToProfile(isUserVerified: Boolean?) {
    navigate(if (isUserVerified != true) Screens.Profile.name else Screens.Verify.name)
}

private fun NavHostController.navigateAndClear(route: String) {
    navigate(route = route) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
    graph.setStartDestination(route)
}

private fun UserData?.getStartDestination() = when {
    this == null -> Screens.Authentication.name
    this.isVerified != true -> Screens.Profile.name
    this.isVerified != false -> Screens.Verify.name
    else -> Screens.Authentication.name
}
