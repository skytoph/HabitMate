package com.skytoph.taski.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skytoph.taski.presentation.nav.MainScreen

object Graph {
    const val ROOT = "root_graph"
    const val HABITS = "habits_graph"
    const val SETTINGS = "settings_graph"
}

@Composable
fun HabitMateApp(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.HABITS
    ) {
        composable(route = Graph.HABITS) {
            MainScreen()
        }
    }
}