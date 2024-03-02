package com.github.skytoph.taski.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.github.skytoph.taski.presentation.auth.authentication.AuthViewModel
import com.github.skytoph.taski.presentation.nav.authNavigation
import com.github.skytoph.taski.presentation.nav.mainNavigation

object Graph {
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val HABITS = "habits_graph"
}

@Composable
fun HabitMateApp(
    navController: NavHostController = rememberNavController(),
    viewModel: AuthViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            route = Graph.ROOT,
            startDestination = viewModel.startDestination()
        ) {
            authNavigation(navController, viewModel)
            mainNavigation(navController)
        }
    }
}