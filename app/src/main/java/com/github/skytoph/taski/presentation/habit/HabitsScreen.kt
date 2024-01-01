package com.github.skytoph.taski.presentation.habit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.SportsGymnastics
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.presentation.habit.component.HabitList
import com.github.skytoph.taski.ui.theme.TaskiTheme


@Composable
fun HabitsScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    onNavigate: () -> Unit
){
    HabitList(habits = viewModel.habits())
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitPreview() {
    TaskiTheme {
        val habits = listOf(
            HabitUi("dev", Icons.Outlined.AcUnit, Color.Red, listOf(), 349),
            HabitUi("yoga", Icons.Outlined.SportsGymnastics, Color.Blue, listOf(348), 349),
        )
        HabitList(habits)
    }
}