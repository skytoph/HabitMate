@file:OptIn(ExperimentalFoundationApi::class)

package com.skytoph.taski.presentation.habit.list.component

import androidx.compose.animation.Crossfade
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.skytoph.taski.R
import com.skytoph.taski.core.datastore.settings.ViewType
import com.skytoph.taski.presentation.core.preview.HabitsProvider
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.skytoph.taski.presentation.habit.list.HistoryUi
import com.skytoph.taski.ui.theme.HabitMateTheme

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
    ListOfHabits(
        habits = habits,
        view = view,
        updateView = updateView,
        onClick = onClick,
        onLongClick = onLongClick,
        onDone = onDone
    )
}

@Composable
fun ListOfHabits(
    habits: List<HabitWithHistoryUi<HistoryUi>>,
    view: ViewType,
    updateView: (Int) -> Unit,
    onClick: (HabitUi) -> Unit,
    onLongClick: (HabitUi) -> Unit,
    onDone: (HabitUi, Int) -> Unit,
) {
    Crossfade(targetState = view) { view ->
        when (view) {
            is ViewType.Calendar -> ListOfHabitsCalendar(
                habits = habits,
                columns = view.entries.div(7),
                onClick = onClick,
                onLongClick = onLongClick,
                onDone = onDone
            )

            is ViewType.Daily -> ListOfHabitsDaily(
                habits = habits,
                view = view,
                updateView = updateView,
                onClick = onClick,
                onLongClick = onLongClick,
                onDone = onDone
            )
        }
    }
}

@Composable
fun ListOfHabitsDaily(
    habits: List<HabitWithHistoryUi<HistoryUi>>,
    view: ViewType.Daily,
    updateView: (Int) -> Unit,
    onClick: (HabitUi) -> Unit,
    onLongClick: (HabitUi) -> Unit,
    onDone: (HabitUi, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = context.getString(R.string.content_description_list_of_habits) }
            .padding(horizontal = dimensionResource(id = R.dimen.main_padding)),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            WeekDayLabelsCard(modifier = Modifier.animateItem(), entries = view.entries)
        }
        items(items = habits, key = { it.habit.id }) { habit ->
            HabitDaily(
                modifier = modifier.widthIn(max = 520.dp),
                onDone = onDone,
                habit = habit.habit,
                history = habit.history,
                updateEntries = updateView,
                onClick = { onClick(habit.habit) },
                onLongClick = { onLongClick(habit.habit) }
            )
        }
    }
}

@Composable
fun ListOfHabitsCalendar(
    habits: List<HabitWithHistoryUi<HistoryUi>>,
    columns: Int = 50,
    onClick: (HabitUi) -> Unit,
    onLongClick: (HabitUi) -> Unit,
    onDone: (HabitUi, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = context.getString(R.string.content_description_list_of_habits) }
            .padding(horizontal = dimensionResource(id = R.dimen.main_padding)),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = habits, key = { it.habit.id }) { habit ->
            HabitCalendar(
                modifier = modifier.widthIn(max = 520.dp),
                onDone = { onDone(habit.habit, 0) },
                habit = habit.habit,
                history = habit.history,
                onClick = { onClick(habit.habit) },
                onLongClick = { onLongClick(habit.habit) }
            )
        }
    }
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