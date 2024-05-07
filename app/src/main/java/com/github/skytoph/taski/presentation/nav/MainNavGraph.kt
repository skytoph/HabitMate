package com.github.skytoph.taski.presentation.nav

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.skytoph.taski.presentation.Graph
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

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HabitScreens.HabitList.route,
        route = Graph.HABITS
    ) {
        composable(route = HabitScreens.HabitList.route,
            exitTransition = { fadeOut(tween(delayMillis = 90)) }) {
            HabitsScreen(
                onCreateHabit = { navController.navigate(HabitScreens.CreateHabit.route) },
                onHabitClick = { habit -> navController.navigate(HabitScreens.HabitDetails(habit.id.toString()).route) })
        }

        composable(
            route = HabitScreens.CreateHabit.route,
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { fadeOut(tween(delayMillis = 90)) },
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
            exitTransition = { fadeOut(tween(delayMillis = 90)) },
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
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { fadeOut(tween(delayMillis = 90)) },
            popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = null,
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