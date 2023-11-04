package com.github.skytoph.taski.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.skytoph.taski.presentation.auth.AuthenticationScreen
import com.github.skytoph.taski.presentation.profile.ProfileScreen
import com.github.skytoph.taski.presentation.auth.signin.SignInScreen
import com.github.skytoph.taski.presentation.auth.signup.SignUpScreen

enum class Screens {
    Authentication,
    SignIn,
    SignUp,
    Verify,
    Profile,
}

@Composable
fun TaskiApp(navController: NavHostController = rememberNavController()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(navController = navController, startDestination = Screens.Authentication.name) {
            composable(route = Screens.Authentication.name) {
                AuthenticationScreen(
                    viewModel = viewModel(),
                    onSignInClick = { navController.navigate(Screens.SignIn.name) },
                    onSignUpClick = { navController.navigate(Screens.SignUp.name) },
                    onSignInWithGoogleClick = { navController.navigate(Screens.Profile.name) },
                )
            }
            composable(route = Screens.SignIn.name) {
                SignInScreen(viewModel = viewModel()) {  }
            }
            composable(route = Screens.SignUp.name) {
                SignUpScreen(viewModel = viewModel()) {  }
            }
//            composable(route = Screens.Profile.name) {
//                ProfileScreen()
//            }
        }
    }
}