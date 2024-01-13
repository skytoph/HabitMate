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
import com.github.skytoph.taski.presentation.habit.create.IconsColors
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun HabitList(
    modifier: Modifier = Modifier,
    habits: List<HabitUi>,
    onDoneHabit: (HabitUi) -> Unit,
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = habits, key = { habit -> habit.id }) { habit ->
            HabitCard(habit = habit, onDone = { onDoneHabit(habit) })
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitListPreview() {
    TaskiTheme {
        val habits = listOf(
            HabitUi(0, "dev", 1, Icons.Outlined.Code, IconsColors.allColors.first(), listOf(), 349),
            HabitUi(
                0,
                "yoga",
                1,
                Icons.Outlined.SportsGymnastics,
                IconsColors.allColors.last(),
                listOf(348),
                349
            ),
        )
        HabitList(habits = habits, onDoneHabit = {})
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DarkHabitListPreview() {
    TaskiTheme(darkTheme = true) {
        val habits = listOf(
            HabitUi(0, "dev", 1, Icons.Outlined.Code, Color.Red, listOf(340, 341, 344), 349),
            HabitUi(
                0,
                "yoga",
                1,
                Icons.Outlined.SportsGymnastics,
                Color.Blue,
                listOf(348, 349),
                349
            ),
        )
        HabitList(habits = habits, onDoneHabit = {})
    }
}