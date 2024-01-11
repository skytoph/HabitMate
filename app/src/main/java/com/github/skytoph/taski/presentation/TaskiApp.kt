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
import com.github.skytoph.taski.presentation.habit.create.EditHabitViewModel
import com.github.skytoph.taski.presentation.habit.create.component.EditHabitScreen
import com.github.skytoph.taski.presentation.habit.create.component.SelectIconScreen
import com.github.skytoph.taski.presentation.habit.list.component.HabitsScreen
import com.github.skytoph.taski.presentation.profile.ProfileScreen

private enum class AuthScreens {
    Authentication,
    SignIn,
    SignUp,
    Verify,
}

private enum class HabitScreens {
    Profile,
    HabitList,
    CreateHabit,
    SelectIcon,
}

@Composable
fun TaskiApp(
    navController: NavHostController = rememberNavController(),
    viewModel: AuthViewModel = hiltViewModel()
) {
    val editHabitViewModel: EditHabitViewModel = hiltViewModel()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val start = viewModel.currentUser().getStartDestination()

        NavHost(navController = navController, startDestination = start) {
            composable(route = AuthScreens.Authentication.name) {
                AuthenticationScreen(
                    onSignInClick = { navController.navigate(AuthScreens.SignIn.name) },
                    onSignUpClick = { navController.navigate(AuthScreens.SignUp.name) },
                )
            }
            composable(route = AuthScreens.SignIn.name) {
                SignInScreen(
                    onNavigate = {
                        navController.navigateToProfile(viewModel.currentUser()?.isVerified)
                    }
                )
            }
            composable(route = AuthScreens.SignUp.name) {
                SignUpScreen(
                    onNavigate = {
                        navController.navigateToProfile(viewModel.currentUser()?.isVerified)
                    })
            }
            composable(route = AuthScreens.Verify.name) {
                VerificationScreen(
                    onNavigate = {
                        navController.navigateAndClear(route = HabitScreens.HabitList.name)
                    }, navigateUp = {
                        if (navController.previousBackStackEntry != null) navController.navigateUp()
                        else navController.navigateAndClear(AuthScreens.Authentication.name)
                    })
            }
            composable(route = HabitScreens.Profile.name) {
                ProfileScreen(onSignOut = { navController.signOut(viewModel) })
            }
            composable(route = HabitScreens.HabitList.name) {
                HabitsScreen(onCreateHabit = {
                    navController.navigate(HabitScreens.CreateHabit.name)
                })
            }
            composable(route = HabitScreens.CreateHabit.name) {
                EditHabitScreen(
                    navigateUp = navController::navigateUp,
                    onSelectIconClick = { navController.navigate(HabitScreens.SelectIcon.name) },
                    viewModel = editHabitViewModel
                )
            }
            composable(route = HabitScreens.SelectIcon.name) {
                SelectIconScreen(
                    navigateUp = navController::navigateUp, viewModel = editHabitViewModel
                )
            }
        }
    }
}

private fun NavHostController.signOut(viewModel: AuthViewModel) {
    viewModel.signOut()
    navigateAndClear(AuthScreens.Authentication.name)
}

private fun NavHostController.navigateToProfile(isUserVerified: Boolean?) {
    if (isUserVerified == true) navigateAndClear(HabitScreens.Profile.name) else navigate(
        AuthScreens.Verify.name
    )
}

private fun NavHostController.navigateAndClear(route: String) {
    navigate(route = route) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
    graph.setStartDestination(route)
}

private fun UserData?.getStartDestination() = when {
    this == null -> AuthScreens.Authentication.name
    this.isVerified == false -> AuthScreens.Verify.name
    this.isVerified == true -> HabitScreens.HabitList.name
    else -> AuthScreens.Authentication.name
}
