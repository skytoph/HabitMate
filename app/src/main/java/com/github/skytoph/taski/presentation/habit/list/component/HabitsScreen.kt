package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.SportsGymnastics
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.HabitsViewModel
import com.github.skytoph.taski.ui.theme.TaskiTheme


@Composable
fun HabitsScreen(
    viewModel: HabitsViewModel = hiltViewModel(),
    onCreateHabit: () -> Unit
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = onCreateHabit,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = Icons.Default.Add.name)
        }
    }) { paddingValues ->
        HabitList(modifier = Modifier.padding(paddingValues), habits = viewModel.habits())
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitScreenPreview() {
    TaskiTheme {
        HabitsScreen(onCreateHabit = {})
    }
}