package com.github.skytoph.taski.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.skytoph.taski.presentation.auth.authentication.AuthViewModel
import com.github.skytoph.taski.presentation.nav.MainScreen
import com.github.skytoph.taski.presentation.nav.authNavigation

object Graph {
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val HABITS = "habits_graph"
}

@Composable
fun HabitMateApp(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.HABITS //viewModel.startDestination()
    ) {
        authNavigation(navController, authViewModel)
        composable(route = Graph.HABITS) {
            MainScreen()
        }
    }
}