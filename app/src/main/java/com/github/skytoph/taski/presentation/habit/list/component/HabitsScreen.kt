package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.presentation.habit.list.HabitsViewModel
import com.github.skytoph.taski.ui.theme.TaskiTheme


@Composable
fun HabitsScreen(
    viewModel: HabitsViewModel = hiltViewModel(),
    onCreateHabit: () -> Unit
) {
    val state = viewModel.state()
    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = onCreateHabit,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = Icons.Default.Add.name)
        }
    }) { paddingValues ->
        HabitList(
            modifier = Modifier.padding(paddingValues),
            habits = state.value.habits,
            onDoneHabit = { habit -> viewModel.habitDone(habit) })
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitScreenPreview() {
    TaskiTheme(darkTheme = true) {
        HabitsScreen(onCreateHabit = {})
    }
}