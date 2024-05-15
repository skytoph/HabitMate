package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.preview.HabitsProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.view.ViewType
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun HabitList(
    modifier: Modifier = Modifier,
    view: ViewType = ViewType.Calendar(),
    habits: List<HabitWithHistoryUi<HistoryUi>>,
    onDoneHabit: (HabitUi, Int) -> Unit = { _, _ -> },
    onHabitClick: (HabitUi) -> Unit = {},
    updateViewState: (Int) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.padding(dimensionResource(id = R.dimen.main_padding)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = habits, key = { it.habit.id }) { habitWithHistory ->
            HabitCard(view, habitWithHistory, updateViewState, onHabitClick, onDoneHabit)
        }
    }
}

@Composable
private fun HabitCard(
    view: ViewType,
    habitWithHistory: HabitWithHistoryUi<HistoryUi>,
    updateViewState: (Int) -> Unit,
    onHabitClick: (HabitUi) -> Unit,
    onDoneHabit: (HabitUi, Int) -> Unit
) {
    if (view is ViewType.Daily)
        HabitDaily(
            modifier = Modifier
                .clickable { onHabitClick(habitWithHistory.habit) }
                .semantics { contentDescription = "habit" },
            onDone = onDoneHabit,
            habit = habitWithHistory.habit,
            history = habitWithHistory.history,
            updateEntries = updateViewState
        )
    else
        HabitCalendar(
            modifier = Modifier
                .clickable { onHabitClick(habitWithHistory.habit) }
                .semantics { contentDescription = "habit" },
            onDone = { onDoneHabit(habitWithHistory.habit, 0) },
            habit = habitWithHistory.habit,
            history = habitWithHistory.history,
            updateEntries = updateViewState
        )
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitListPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme {
        HabitList(habits = habits)
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DarkHabitListPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme(darkTheme = true) {
        HabitList(habits = habits)
    }
}