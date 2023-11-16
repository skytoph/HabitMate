package com.github.skytoph.taski.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.skytoph.taski.presentation.auth.authentication.AuthViewModel
import com.github.skytoph.taski.presentation.auth.authentication.AuthenticationScreen
import com.github.skytoph.taski.presentation.auth.authentication.user.UserData
import com.github.skytoph.taski.presentation.auth.signin.SignInScreen
import com.github.skytoph.taski.presentation.auth.signup.SignUpScreen
import com.github.skytoph.taski.presentation.auth.verify.VerificationScreen
import com.github.skytoph.taski.presentation.profile.ProfileScreen

private enum class Screens {
    Authentication,
    SignIn,
    SignUp,
    Verify,
    Profile,
}

@Composable
fun TaskiApp(navController: NavHostController = rememberNavController()) {
    val viewModel: AuthViewModel = hiltViewModel()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val start = viewModel.currentUser().getStartDestination()

        NavHost(navController = navController, startDestination = start) {
            composable(route = Screens.Authentication.name) {
                AuthenticationScreen(
                    onSignInClick = { navController.navigate(Screens.SignIn.name) },
                    onSignUpClick = { navController.navigate(Screens.SignUp.name) },
                )
            }
            composable(route = Screens.SignIn.name) {
                SignInScreen(
                    onNavigate = {
                        navController.navigateToProfile(viewModel.currentUser()?.isVerified)
                    }
                )
            }
            composable(route = Screens.SignUp.name) {
                SignUpScreen(
                    onNavigate = {
                        navController.navigateToProfile(viewModel.currentUser()?.isVerified)
                    })
            }
            composable(route = Screens.Verify.name) {
                VerificationScreen(
                    onNavigate = {
                        navController.navigateAndClear(route = Screens.Profile.name)
                    }, navigateUp = {
                        if (navController.previousBackStackEntry != null) navController.navigateUp()
                        else navController.navigateAndClear(Screens.Authentication.name)
                    })
            }
            composable(route = Screens.Profile.name) {
                ProfileScreen {
                    viewModel.signOut()
                    navController.navigateAndClear(Screens.Authentication.name)
                }
            }
        }
    }
}

private fun NavHostController.navigateToProfile(isUserVerified: Boolean?) {
    if (isUserVerified == true) navigateAndClear(Screens.Profile.name) else navigate(Screens.Verify.name)
}

private fun NavHostController.navigateAndClear(route: String) {
    navigate(route = route) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
    graph.setStartDestination(route)
}

private fun UserData?.getStartDestination() = when {
    this == null -> Screens.Authentication.name
    this.isVerified == true -> Screens.Profile.name
    this.isVerified == false -> Screens.Verify.name
    else -> Screens.Authentication.name
}
