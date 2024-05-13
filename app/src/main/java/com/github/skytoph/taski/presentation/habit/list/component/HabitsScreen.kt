package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.AppBarAction
import com.github.skytoph.taski.presentation.core.component.LoadingCirclesFullscreen
import com.github.skytoph.taski.presentation.core.preview.HabitsProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HabitListEvent
import com.github.skytoph.taski.presentation.habit.list.HabitListState
import com.github.skytoph.taski.presentation.habit.list.HabitsView
import com.github.skytoph.taski.presentation.habit.list.HabitsViewModel
import com.github.skytoph.taski.presentation.habit.list.HabitsViewOption
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme


@Composable
fun HabitsScreen(
    viewModel: HabitsViewModel = hiltViewModel(),
    onCreateHabit: () -> Unit,
    onHabitClick: (HabitUi) -> Unit,
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    LaunchedEffect(Unit) {
        val actionAdd = AppBarAction.add.copy(color = onSurface, onClick = onCreateHabit)
        val actionView = AppBarAction.view.copy(color = onSurface,
            onClick = { viewModel.onEvent(HabitListEvent.ShowMenu) })
        viewModel.initAppBar(canNavigateUp = false, menuItems = listOf(actionView, actionAdd))
    }

    val view = viewModel.view.collectAsState()

    val state = viewModel.state()

    Column {
        if (state.value.isViewTypeVisible)
            ViewTypeSelection(onSelectOption = { option ->
                viewModel.onEvent(HabitListEvent.UpdateView(option.view))
            })
        Habits(
            state = state,
            view = view,
            onHabitClick = onHabitClick,
            onHabitDone = { habit, daysAgo -> viewModel.habitDone(habit, daysAgo) },
            updateState = { entries ->
                val updatedView = view.value.withEntries(entries)
                viewModel.onEvent(HabitListEvent.UpdateView(updatedView))
            }
        )
    }
}

@Composable
private fun ViewTypeSelection(
    viewOptions: List<HabitsViewOption> = HabitsViewOption.list,
    onSelectOption: (HabitsViewOption) -> Unit = {}
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.main_padding))
    ) {
        items(viewOptions) { option ->
            Button(onClick = { onSelectOption(option) }) {
                Text(text = option.title.getString(LocalContext.current))
            }
        }
    }
}

@Composable
private fun Habits(
    state: State<HabitListState>,
    view: State<HabitsView>,
    updateState: (Int) -> Unit,
    onHabitClick: (HabitUi) -> Unit = {},
    onHabitDone: (HabitUi, Int) -> Unit = { _, _ -> }
) {
    if (state.value.isLoading) LoadingCirclesFullscreen()
    else HabitList(
        habits = state.value.habits,
        view = view.value,
        onDoneHabit = onHabitDone,
        onHabitClick = onHabitClick,
        updateViewState = updateState
    )
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitScreenPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme(darkTheme = true) {
        ViewTypeSelection()
    }
}