package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.presentation.core.preview.HabitsProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun HabitList(
    modifier: Modifier = Modifier,
    habits: List<HabitWithHistoryUi<HistoryUi>>,
    onDoneHabit: (HabitUi) -> Unit,
    onHabitClick: (HabitUi) -> Unit,
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = habits, key = { it.habit.id }) {
            HabitCard(
                habit = it.habit,
                history = it.history,
                onDone = { onDoneHabit(it.habit) },
                modifier = Modifier.clickable { onHabitClick(it.habit) },
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitListPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme {
        HabitList(habits = habits, onDoneHabit = {}) {}
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DarkHabitListPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme(darkTheme = true) {
        HabitList(habits = habits, onDoneHabit = {}) {}
    }
}