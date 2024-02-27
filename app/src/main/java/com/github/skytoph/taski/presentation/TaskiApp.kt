package com.github.skytoph.taski.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.github.skytoph.taski.presentation.auth.authentication.AuthViewModel
import com.github.skytoph.taski.presentation.auth.authentication.AuthenticationScreen
import com.github.skytoph.taski.presentation.auth.authentication.user.UserData
import com.github.skytoph.taski.presentation.auth.signin.SignInScreen
import com.github.skytoph.taski.presentation.auth.signup.SignUpScreen
import com.github.skytoph.taski.presentation.auth.verify.VerificationScreen
import com.github.skytoph.taski.presentation.core.nav.ScaleTransitionDirection
import com.github.skytoph.taski.presentation.core.nav.scaleIntoContainer
import com.github.skytoph.taski.presentation.core.nav.scaleOutOfContainer
import com.github.skytoph.taski.presentation.habit.HabitScreens
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.create.component.CreateHabitScreen
import com.github.skytoph.taski.presentation.habit.details.components.HabitDetailsScreen
import com.github.skytoph.taski.presentation.habit.edit.component.EditHabitScreen
import com.github.skytoph.taski.presentation.habit.icon.component.SelectIconScreen
import com.github.skytoph.taski.presentation.habit.list.component.HabitsScreen
import com.github.skytoph.taski.presentation.profile.ProfileScreen

private abstract class AuthScreens(val route: String) {
    object Authentication : AuthScreens("authentication")
    object SignIn : AuthScreens("sign_in")
    object SignUp : AuthScreens("sign_up")
    object Verify : AuthScreens("verify")

    companion object {
        const val route = "auth"
    }
}

@Composable
fun TaskiApp(
    navController: NavHostController = rememberNavController(),
    viewModel: AuthViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // todo replace with viewModel.currentUser().getStartDestination()
        val start = HabitScreens.HabitList.route

        NavHost(navController = navController, startDestination = start) {
            navigation(startDestination = start, route = AuthScreens.route) {
                composable(route = AuthScreens.Authentication.route) {
                    AuthenticationScreen(
                        onSignInClick = { navController.navigate(AuthScreens.SignIn.route) },
                        onSignUpClick = { navController.navigate(AuthScreens.SignUp.route) },
                    )
                }
                composable(route = AuthScreens.SignIn.route) {
                    SignInScreen(
                        onNavigate = {
                            navController.navigateToProfile(viewModel.currentUser()?.isVerified)
                        }
                    )
                }
                composable(route = AuthScreens.SignUp.route) {
                    SignUpScreen(
                        onNavigate = {
                            navController.navigateToProfile(viewModel.currentUser()?.isVerified)
                        })
                }
                composable(route = AuthScreens.Verify.route) {
                    VerificationScreen(
                        onNavigate = {
                            navController.navigateAndClear(route = HabitScreens.HabitList.route)
                        }, navigateUp = {
                            if (navController.previousBackStackEntry != null) navController.navigateUp()
                            else navController.navigateAndClear(AuthScreens.Authentication.route)
                        })
                }
                composable(route = HabitScreens.Profile.route) {
                    ProfileScreen(onSignOut = { navController.signOut(viewModel) })
                }
            }

            composable(route = HabitScreens.HabitList.route) {
                HabitsScreen(
                    onCreateHabit = { navController.navigate(HabitScreens.CreateHabit.route) },
                    onHabitClick = { habit -> navController.navigate(HabitScreens.HabitDetails(habit.id.toString()).route) })
            }
            composable(
                route = HabitScreens.CreateHabit.route,
                enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
                popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
                popEnterTransition = null,
            ) {
                CreateHabitScreen(navigateUp = navController::navigateUp,
                    onSelectIconClick = { navController.navigate(HabitScreens.SelectIcon.route) })
            }
            composable(
                route = HabitScreens.HabitDetails.baseRoute,
                arguments = listOf(navArgument(name = HabitScreens.HabitDetails.habitIdArg) {
                    type = NavType.LongType
                }),
                enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
                popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
                popEnterTransition = null,
            ) {
                val habitId = it.arguments?.getLong(HabitScreens.HabitDetails.habitIdArg).toString()
                HabitDetailsScreen(
                    navigateUp = navController::navigateUp,
                    onEditHabit = { navController.navigate(HabitScreens.EditHabit(habitId).route) }
                )
            }
            composable(
                route = HabitScreens.EditHabit.baseRoute,
                arguments = listOf(navArgument(name = HabitScreens.EditHabit.habitIdArg) {
                    type = NavType.LongType
                    defaultValue = HabitUi.ID_DEFAULT
                }),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up, tween(500)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down, tween(500)
                    )
                },
                popEnterTransition = null
            ) {
                EditHabitScreen(
                    navigateUp = navController::navigateUp,
                    onSelectIconClick = { navController.navigate(HabitScreens.SelectIcon.route) },
                )
            }
            composable(
                route = HabitScreens.SelectIcon.route,
                enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
                popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
                popEnterTransition = null,
            ) {
                SelectIconScreen(navigateUp = navController::navigateUp)
            }
        }
    }
}

private fun NavHostController.signOut(viewModel: AuthViewModel) {
    viewModel.signOut()
    navigateAndClear(AuthScreens.Authentication.route)
}

private fun NavHostController.navigateToProfile(isUserVerified: Boolean?) {
    if (isUserVerified == true) navigateAndClear(HabitScreens.Profile.route) else navigate(
        AuthScreens.Verify.route
    )
}

private fun NavHostController.navigateAndClear(route: String) {
    navigate(route = route) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
    graph.setStartDestination(route)
}

private fun UserData?.getStartDestination() = when {
    this == null -> AuthScreens.Authentication.route
    this.isVerified == false -> AuthScreens.Verify.route
    this.isVerified == true -> HabitScreens.HabitList.route
    else -> AuthScreens.Authentication.route
}
