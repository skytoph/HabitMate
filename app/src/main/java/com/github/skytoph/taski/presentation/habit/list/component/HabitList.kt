package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.preview.HabitsProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.view.ViewType
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitList(
    modifier: Modifier = Modifier,
    view: ViewType = ViewType.Daily(5),
    habits: List<HabitWithHistoryUi<HistoryUi>>,
    onDone: (HabitUi, Int) -> Unit = { _, _ -> },
    onClick: (HabitUi) -> Unit = {},
    onLongClick: (HabitUi) -> Unit = {},
    updateView: (Int) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.main_padding)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (view.matches(ViewType.Daily())) stickyHeader {
            WeekDayLabelsCard(entries = view.entries)
        }
        items(items = habits, key = { it.habit.id }) { habitWithHistory ->
            HabitCard(view, habitWithHistory, updateView, onClick, onLongClick, onDone, Modifier.animateItem())
        }
    }
}

@Composable
private fun HabitCard(
    view: ViewType,
    habitWithHistory: HabitWithHistoryUi<HistoryUi>,
    updateViewState: (Int) -> Unit,
    onHabitClick: (HabitUi) -> Unit,
    onHabitLongClick: (HabitUi) -> Unit = {},
    onDoneHabit: (HabitUi, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (view is ViewType.Daily)
        HabitDaily(
            modifier = modifier,
            onDone = onDoneHabit,
            habit = habitWithHistory.habit,
            history = habitWithHistory.history,
            updateEntries = updateViewState,
            onClick = { onHabitClick(habitWithHistory.habit) },
            onLongClick = { onHabitLongClick(habitWithHistory.habit) }
        )
    else
        HabitCalendar(
            modifier = modifier,
            onDone = { onDoneHabit(habitWithHistory.habit, 0) },
            habit = habitWithHistory.habit,
            history = habitWithHistory.history,
            updateEntries = updateViewState,
            onClick = { onHabitClick(habitWithHistory.habit) },
            onLongClick = { onHabitLongClick(habitWithHistory.habit) }
        )
}

@Composable
@Preview(showBackground = true)
fun HabitListPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme {
        HabitList(habits = habits)
    }
}

@Composable
@Preview(showBackground = true)
fun DarkHabitListPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme(darkTheme = true) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)) { HabitList(habits = habits) }
    }
}