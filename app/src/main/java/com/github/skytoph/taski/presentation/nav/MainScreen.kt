package com.github.skytoph.taski.presentation.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.skytoph.taski.presentation.appbar.MainViewModel
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.component.HabitAppBar
import com.github.skytoph.taski.ui.theme.HabitMateTheme

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
                modifier = Modifier.padding(horizontal = 16.dp),
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

@Composable
private fun SnackbarWithTitle(message: SnackbarMessage) {
    Snackbar(
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = message.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = message.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Icon(
                imageVector = message.icon,
                contentDescription = message.title,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun SnackbarPreview() {
    HabitMateTheme(darkTheme = true) {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarWithTitle(
                message = SnackbarMessage(
                    title = "habit",
                    message = "message...",
                    icon = Icons.Default.Delete
                )
            )
        }
    }
}