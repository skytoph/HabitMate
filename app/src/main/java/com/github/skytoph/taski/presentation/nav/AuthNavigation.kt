package com.github.skytoph.taski.presentation.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.skytoph.taski.presentation.Graph
import com.github.skytoph.taski.presentation.auth.authentication.AuthViewModel
import com.github.skytoph.taski.presentation.auth.authentication.AuthenticationScreen
import com.github.skytoph.taski.presentation.auth.authentication.user.UserData
import com.github.skytoph.taski.presentation.auth.signin.component.SignInScreen
import com.github.skytoph.taski.presentation.auth.signup.component.SignUpScreen
import com.github.skytoph.taski.presentation.auth.verify.VerificationScreen
import com.github.skytoph.taski.presentation.habit.HabitScreens
import com.github.skytoph.taski.presentation.profile.ProfileScreen

private abstract class AuthScreens(val route: String) {
    object Authentication : AuthScreens("authentication")
    object SignIn : AuthScreens("sign_in")
    object SignUp : AuthScreens("sign_up")
    object Verify : AuthScreens("verify")
}

fun NavGraphBuilder.authNavigation(
    navController: NavHostController,
    viewModel: AuthViewModel,
    start: String = viewModel.currentUser().getStartDestination()
) {
    navigation(startDestination = start, route = Graph.AUTH) {
        composable(route = AuthScreens.Authentication.route) {
            AuthenticationScreen(
                onSignInClick = { navController.navigate(AuthScreens.SignIn.route) },
                onSignUpClick = { navController.navigate(AuthScreens.SignUp.route) },
            )
        }
        composable(route = AuthScreens.SignIn.route) {
            SignInScreen(
                onNavigate = { navController.signInSuccessful(viewModel.currentUser()?.isVerified) }
            )
        }
        composable(route = AuthScreens.SignUp.route) {
            SignUpScreen(
                onNavigate = { navController.signInSuccessful(viewModel.currentUser()?.isVerified) })
        }
        composable(route = AuthScreens.Verify.route) {
            VerificationScreen(
                onNavigate = { navController.signInSuccessful(viewModel.currentUser()?.isVerified) },
                navigateUp = {
                    if (navController.previousBackStackEntry != null) navController.navigateUp()
                    else navController.navigateAndClear(AuthScreens.Authentication.route)
                })
        }
        composable(route = HabitScreens.Profile.route) {    //todo move to habits package
            ProfileScreen(onSignOut = { navController.signOut(viewModel) })
        }
    }
}

private fun NavHostController.signOut(viewModel: AuthViewModel) {
    viewModel.signOut()
    navigateAndClear(AuthScreens.Authentication.route)
}

private fun NavHostController.signInSuccessful(isUserVerified: Boolean?) {
    if (isUserVerified == true) {
        popBackStack(graph.startDestinationId, true)
        navigate(Graph.HABITS)
    } else navigate(AuthScreens.Verify.route)
}

private fun NavHostController.navigateAndClear(route: String) {
    navigate(route = route) { popUpTo(graph.startDestinationId) { inclusive = true } }
    graph.setStartDestination(route)
}

private fun UserData?.getStartDestination() = when {
    this == null -> AuthScreens.Authentication.route
    this.isVerified == false -> AuthScreens.Verify.route
    else -> AuthScreens.Authentication.route
}
