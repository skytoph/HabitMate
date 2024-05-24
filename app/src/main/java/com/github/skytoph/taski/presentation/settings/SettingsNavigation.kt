package com.github.skytoph.taski.presentation.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.github.skytoph.taski.presentation.Graph
import com.github.skytoph.taski.presentation.habit.HabitScreens
import com.github.skytoph.taski.presentation.settings.archive.ArchiveScreen
import com.github.skytoph.taski.presentation.settings.menu.SettingsMenuScreen

abstract class SettingsScreens(val route: String) {
    object SettingsList : SettingsScreens("settings")
    object ArchiveList : SettingsScreens("archive")
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
                reorderClick = { controller.navigate(HabitScreens.ReorderHabits.route) }
            )
        }
        composable(route = SettingsScreens.ArchiveList.route) {
            ArchiveScreen()
        }
    }
}