@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.skytoph.taski.presentation.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.skytoph.taski.presentation.appbar.AppBarViewModel
import com.skytoph.taski.presentation.appbar.SnackbarMessage

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: AppBarViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { viewModel.snackbarState() }
    val dismissSnackbarState = rememberDismissState(snackbarHostState)

    val color = MaterialTheme.colorScheme.onSurface
    LaunchedEffect(Unit) {
        viewModel.initState(color)
    }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    Scaffold(
        topBar = {
            HabitAppBar(
                modifier = Modifier.statusBarsPadding().padding(horizontal = 8.dp),
                state = viewModel.state(),
                navigateUp = navController::navigateUp,
                expandList = viewModel::expandList
            )
        },
        snackbarHost = {
            SwipeToDismiss(
                state = dismissSnackbarState,
                background = {},
                dismissContent = {
                    SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                        val visuals = snackbarData.visuals
                        if (visuals is SnackbarMessage) SnackbarWithTitle(visuals)
                        else Snackbar(snackbarData)
                    }
                },
            )
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