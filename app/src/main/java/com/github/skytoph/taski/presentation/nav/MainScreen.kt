package com.github.skytoph.taski.presentation.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.skytoph.taski.presentation.appbar.AppBarViewModel
import com.github.skytoph.taski.presentation.core.component.HabitAppBar

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: AppBarViewModel = hiltViewModel()
) {
    val color = MaterialTheme.colorScheme.onSurface
    LaunchedEffect(Unit) {
        viewModel.initState(color)
    }

    Scaffold(topBar = {
        HabitAppBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            state = viewModel.state(),
            navigateUp = navController::navigateUp
        )
    }) { paddingValue ->
        Box(modifier = Modifier.padding(top = paddingValue.calculateTopPadding())) {
            MainNavGraph(navController)
        }
    }
}