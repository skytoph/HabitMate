package com.github.skytoph.taski.presentation.settings

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.github.skytoph.taski.presentation.Graph
import com.github.skytoph.taski.presentation.core.nav.ScaleTransitionDirection
import com.github.skytoph.taski.presentation.core.nav.scaleIntoContainer
import com.github.skytoph.taski.presentation.core.nav.scaleOutOfContainer
import com.github.skytoph.taski.presentation.settings.archive.component.ArchiveScreen
import com.github.skytoph.taski.presentation.settings.general.GeneralSettingsScreen
import com.github.skytoph.taski.presentation.settings.menu.SettingsMenuScreen
import com.github.skytoph.taski.presentation.settings.reorder.component.HabitReorderScreen

abstract class SettingsScreens(val route: String) {
    object SettingsList : SettingsScreens("settings")
    object General : SettingsScreens("general")
    object ArchiveList : SettingsScreens("archive")
    object ReorderList : SettingsScreens("reorder")
}

fun NavGraphBuilder.settingsNavigation(
    controller: NavHostController,
) {
    navigation(
        startDestination = SettingsScreens.SettingsList.route,
        route = Graph.SETTINGS,
    ) {
        composable(
            route = SettingsScreens.SettingsList.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(500)
                )
            },
            exitTransition = { fadeOut(tween(delayMillis = 50)) },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(500)
                )
            },
            popEnterTransition = { fadeIn(tween(delayMillis = 50)) }
        ) {
            SettingsMenuScreen(
                generalClick = { controller.navigate(SettingsScreens.ArchiveList.route) },
                archiveClick = { controller.navigate(SettingsScreens.ArchiveList.route) },
                reorderClick = { controller.navigate(SettingsScreens.ReorderList.route) },
            )
        }
        composable(
            route = SettingsScreens.General.route,
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = null,
        ) {
            GeneralSettingsScreen()
        }
        composable(
            route = SettingsScreens.ArchiveList.route,
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = null,
        ) {
            ArchiveScreen()
        }
        composable(
            route = SettingsScreens.ReorderList.route,
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = null,
        ) {
            HabitReorderScreen()
        }
    }
}