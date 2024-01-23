package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.SportsGymnastics
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.icon.GreenBright
import com.github.skytoph.taski.presentation.habit.icon.PinkRose
import com.github.skytoph.taski.presentation.habit.list.EntryUi
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun HabitList(
    modifier: Modifier = Modifier,
    habits: List<HabitUi<EntryUi>>,
    onDoneHabit: (HabitUi<EntryUi>) -> Unit,
    onHabitClick: (HabitUi<EntryUi>) -> Unit,
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = habits, key = { it.id }) { habit ->
            HabitCard(
                habit = habit,
                onDone = { onDoneHabit(habit) },
                modifier = Modifier.clickable { onHabitClick(habit) },
                history = habit.history
            )
        }
    }
}

private val habits = listOf(
    HabitUi<EntryUi>(
        0, "dev", 1, Icons.Outlined.Code, GreenBright
    ),
    HabitUi(
        0, "yoga", 1, Icons.Outlined.SportsGymnastics, PinkRose
    ),
)

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitListPreview() {
    TaskiTheme {
        HabitList(habits = habits, onDoneHabit = {}) {}
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DarkHabitListPreview() {
    TaskiTheme(darkTheme = true) {
        HabitList(habits = habits, onDoneHabit = {}) {}
    }
}