package com.github.skytoph.taski.presentation.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.github.skytoph.taski.presentation.Graph
import com.github.skytoph.taski.presentation.core.nav.ScaleTransitionDirection
import com.github.skytoph.taski.presentation.core.nav.scaleIntoContainer
import com.github.skytoph.taski.presentation.core.nav.scaleOutOfContainer
import com.github.skytoph.taski.presentation.settings.archive.component.ArchiveScreen
import com.github.skytoph.taski.presentation.settings.menu.SettingsMenuScreen
import com.github.skytoph.taski.presentation.settings.reorder.component.HabitReorderScreen

abstract class SettingsScreens(val route: String) {
    object SettingsList : SettingsScreens("settings")
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
        composable(route = SettingsScreens.SettingsList.route) {
            SettingsMenuScreen(
                archiveClick = { controller.navigate(SettingsScreens.ArchiveList.route) },
                reorderClick = { controller.navigate(SettingsScreens.ReorderList.route) }
            )
        }
        composable(route = SettingsScreens.ArchiveList.route) {
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