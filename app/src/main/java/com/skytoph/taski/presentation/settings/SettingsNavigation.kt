package com.skytoph.taski.presentation.settings

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.skytoph.taski.presentation.Graph
import com.skytoph.taski.presentation.core.nav.ScaleTransitionDirection
import com.skytoph.taski.presentation.core.nav.scaleIntoContainer
import com.skytoph.taski.presentation.core.nav.scaleOutOfContainer
import com.skytoph.taski.presentation.settings.archive.component.ArchiveScreen
import com.skytoph.taski.presentation.settings.backup.component.BackupScreen
import com.skytoph.taski.presentation.settings.credits.component.CreditsScreen
import com.skytoph.taski.presentation.settings.general.component.GeneralSettingsScreen
import com.skytoph.taski.presentation.settings.menu.component.SettingsMenuScreen
import com.skytoph.taski.presentation.settings.reorder.component.HabitReorderScreen
import com.skytoph.taski.presentation.settings.restore.component.RestoreScreen
import com.skytoph.taski.presentation.settings.theme.component.ThemeSettingsScreen

abstract class SettingsScreens(val route: String) {
    object SettingsList : SettingsScreens("settings")
    object General : SettingsScreens("general")
    object ArchiveList : SettingsScreens("archive")
    object ReorderList : SettingsScreens("reorder")
    object Theme : SettingsScreens("theme")
    object Backup : SettingsScreens("backup")
    object BackupRestore : SettingsScreens("backup_restore")
    object Credits : SettingsScreens("credits")
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
                archiveClick = { controller.navigate(SettingsScreens.ArchiveList.route) },
                reorderClick = { controller.navigate(SettingsScreens.ReorderList.route) },
                generalClick = { controller.navigate(SettingsScreens.General.route) },
                themeClick = { controller.navigate(SettingsScreens.Theme.route) },
                backupClick = { controller.navigate(SettingsScreens.Backup.route) },
                creditsClick = { controller.navigate(SettingsScreens.Credits.route) }
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
        composable(
            route = SettingsScreens.Theme.route,
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = null,
        ) {
            ThemeSettingsScreen()
        }
        composable(
            route = SettingsScreens.Backup.route,
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = null,
        ) {
            BackupScreen(restoreBackup = { controller.navigate(SettingsScreens.BackupRestore.route) })
        }
        composable(
            route = SettingsScreens.BackupRestore.route,
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { fadeOut(tween(delayMillis = 60)) },
            popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = null,
        ) {
            RestoreScreen()
        }
        composable(
            route = SettingsScreens.Credits.route,
            enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            exitTransition = { fadeOut(tween(delayMillis = 60)) },
            popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = null,
        ) {
            CreditsScreen()
        }
    }
}