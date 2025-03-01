package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.datastore.settings.ViewType
import com.github.skytoph.taski.presentation.core.preview.HabitsProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme
import kotlinx.coroutines.delay

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
    var crashlyticsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        crashlyticsVisible = true
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.main_padding)),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (view.matches(ViewType.Daily())) stickyHeader {
            WeekDayLabelsCard(modifier = Modifier.animateItem(), entries = view.entries)
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
            modifier = modifier.widthIn(max = 520.dp),
            onDone = onDoneHabit,
            habit = habitWithHistory.habit,
            history = habitWithHistory.history,
            updateEntries = updateViewState,
            onClick = { onHabitClick(habitWithHistory.habit) },
            onLongClick = { onHabitLongClick(habitWithHistory.habit) }
        )
    else
        HabitCalendar(
            modifier = modifier.widthIn(max = 520.dp),
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
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) { HabitList(habits = habits) }
    }
}

@Composable
@Preview(showBackground = true)
fun DarkHabitListPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme(darkTheme = true) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) { HabitList(habits = habits) }
    }
}