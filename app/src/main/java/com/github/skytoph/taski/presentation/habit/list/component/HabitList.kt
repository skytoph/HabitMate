package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.SportsGymnastics
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun HabitList(modifier: Modifier = Modifier, habits: List<HabitUi>) {
    LazyColumn(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(habits) { habit ->
            HabitCard(habit)
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitListPreview() {
    TaskiTheme {
        val habits = listOf(
            HabitUi("dev", 1, Icons.Outlined.Code, Color.Red, listOf(), 349),
            HabitUi("yoga", 1, Icons.Outlined.SportsGymnastics, Color.Blue, listOf(348), 349),
        )
        HabitList(habits = habits)
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DarkHabitListPreview() {
    TaskiTheme(darkTheme = true) {
        val habits = listOf(
            HabitUi("dev", 1, Icons.Outlined.Code, Color.Red, listOf(340, 341, 344), 349),
            HabitUi("yoga", 1, Icons.Outlined.SportsGymnastics, Color.Blue, listOf(348, 349), 349),
        )
        HabitList(habits = habits)
    }
}