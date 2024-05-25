package com.github.skytoph.taski.presentation.nav

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
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
import com.github.skytoph.taski.presentation.settings.SettingsScreens
import com.github.skytoph.taski.presentation.settings.settingsNavigation

@Composable
fun MainNavGraph(controller: NavHostController) {
    NavHost(
        navController = controller,
        startDestination = HabitScreens.HabitList.route,
        route = Graph.HABITS,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = HabitScreens.HabitList.route,
            exitTransition = { fadeOut(tween(delayMillis = 60)) },
            popEnterTransition = { fadeIn(tween(delayMillis = 60)) }) { backStackEntry ->
            val stateDelete = backStackEntry.collectByKey<Long>(HabitScreens.HabitList.keyDelete)
            val stateArchive = backStackEntry.collectByKey<Long>(HabitScreens.HabitList.keyDelete)

            HabitsScreen(
                deleteState = stateDelete,
                archiveState = stateArchive,
                removeHabitFromState = { backStackEntry.savedStateHandle.remove<Long>(it) },
                onCreateHabit = { controller.navigate(HabitScreens.CreateHabit.route) },
                onHabitClick = { habitId -> controller.navigate(HabitScreens.HabitDetails(habitId.toString()).route) },
                onReorderHabits = { controller.navigate(SettingsScreens.ReorderList.route) },
                onEditHabit = { habitId -> controller.navigate(HabitScreens.EditHabit(habitId.toString()).route) },
                onSettingsClick = { controller.navigate(Graph.SETTINGS) }
            )
        }

        composable(
            route = HabitScreens.CreateHabit.route,
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { fadeOut(tween(delayMillis = 60)) },
            popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = null,
        ) {
            CreateHabitScreen(navigateUp = controller::navigateUp,
                onSelectIconClick = { controller.navigate(HabitScreens.SelectIcon.route) })
        }

        composable(
            route = HabitScreens.HabitDetails.baseRoute,
            arguments = listOf(navArgument(name = HabitScreens.HabitDetails.habitIdArg) {
                type = NavType.LongType
            }),
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { fadeOut(tween(delayMillis = 60)) },
            popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = null,
        ) {
            val habitId = it.arguments?.getLong(HabitScreens.HabitDetails.habitIdArg)
            HabitDetailsScreen(
                onEditHabit = { controller.navigate(HabitScreens.EditHabit(habitId.toString()).route) },
                onDeleteHabit = { controller.setAction(HabitScreens.HabitList.keyDelete, habitId) },
            )
        }

        composable(
            route = HabitScreens.EditHabit.baseRoute,
            arguments = listOf(navArgument(name = HabitScreens.EditHabit.habitIdArg) {
                type = NavType.LongType
                defaultValue = HabitUi.ID_DEFAULT
            }),
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { fadeOut(tween(delayMillis = 60)) },
            popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = null,
        ) {
            EditHabitScreen(
                navigateUp = controller::navigateUp,
                onSelectIconClick = { controller.navigate(HabitScreens.SelectIcon.route) },
            )
        }

        composable(
            route = HabitScreens.SelectIcon.route,
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = null,
        ) {
            SelectIconScreen(navigateUp = controller::navigateUp)
        }

        settingsNavigation(controller)
    }
}

@Composable
private fun <T> NavBackStackEntry.collectByKey(key: String) =
    savedStateHandle.getStateFlow<T?>(key, null).collectAsState()

private fun NavHostController.setAction(key: String, habitId: Long?) {
    previousBackStackEntry?.savedStateHandle?.set(key, habitId)
    navigateUp()
}