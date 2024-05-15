package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.presentation.core.component.AppBarAction
import com.github.skytoph.taski.presentation.core.component.LoadingCirclesFullscreen
import com.github.skytoph.taski.presentation.core.preview.HabitsProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HabitListEvent
import com.github.skytoph.taski.presentation.habit.list.HabitListState
import com.github.skytoph.taski.presentation.habit.list.HabitsViewModel
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.presentation.habit.list.view.ViewType
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
            onClick = { viewModel.onEvent(HabitListEvent.ShowViewType(true)) })
        viewModel.initAppBar(canNavigateUp = false, menuItems = listOf(actionAdd, actionView))
    }

    val view = viewModel.view.collectAsState()
    val state = viewModel.state()

    Column {
        if (state.value.isViewTypeVisible)
            ViewBottomSheet(
                view = view.value,
                hideBottomSheet = { viewModel.onEvent(HabitListEvent.ShowViewType(false)) },
                selectViewType = { viewModel.onEvent(HabitListEvent.UpdateView(viewType = it)) },
                selectSorting = { viewModel.onEvent(HabitListEvent.UpdateView(sortBy = it)) },
                selectFilter = { viewModel.onEvent(HabitListEvent.UpdateView(filterBy = it)) }
            )
        Habits(
            state = state,
            view = view.value.viewType.item,
            onHabitClick = onHabitClick,
            onHabitDone = { habit, daysAgo -> viewModel.habitDone(habit, daysAgo) },
            updateState = { entries -> viewModel.onEvent(HabitListEvent.UpdateEntries(entries)) }
        )
    }
}

@Composable
private fun Habits(
    state: State<HabitListState>,
    view: ViewType,
    updateState: (Int) -> Unit,
    onHabitClick: (HabitUi) -> Unit = {},
    onHabitDone: (HabitUi, Int) -> Unit = { _, _ -> }
) {
    if (state.value.isLoading) LoadingCirclesFullscreen()
    else HabitList(
        habits = state.value.habits,
        view = view,
        onDoneHabit = onHabitDone,
        onHabitClick = onHabitClick,
        updateViewState = updateState
    )
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitScreenPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme(darkTheme = true) {
        HabitList(habits = habits)
    }
}