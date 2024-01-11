package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
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
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(habits) { habit ->
            HabitCard(habit)
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitPreview() {
    TaskiTheme {
        val habits = listOf(
            HabitUi("dev", 1, Icons.Outlined.AcUnit, Color.Red, listOf(), 349),
            HabitUi("yoga", 1, Icons.Outlined.SportsGymnastics, Color.Blue, listOf(348), 349),
        )
        HabitList(habits = habits)
    }
}