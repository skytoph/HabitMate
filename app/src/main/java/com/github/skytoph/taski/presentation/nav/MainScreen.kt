package com.github.skytoph.taski.presentation.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.skytoph.taski.presentation.appbar.MainViewModel
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.component.HabitAppBar

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { viewModel.snackbarState() }

    val color = MaterialTheme.colorScheme.onSurface
    LaunchedEffect(Unit) {
        viewModel.initState(color)
    }

    Scaffold(
        topBar = {
            HabitAppBar(
                modifier = Modifier.padding(horizontal = 8.dp),
                state = viewModel.state(),
                navigateUp = navController::navigateUp
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                val visuals = snackbarData.visuals
                if (visuals is SnackbarMessage)
                    SnackbarWithTitle(visuals)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValue.calculateTopPadding())
        ) {
            MainNavGraph(navController)
        }
    }
}