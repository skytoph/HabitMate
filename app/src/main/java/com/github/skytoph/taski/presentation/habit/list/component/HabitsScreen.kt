package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.AppBarAction
import com.github.skytoph.taski.presentation.core.component.LoadingCirclesFullscreen
import com.github.skytoph.taski.presentation.core.preview.HabitsProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.details.components.DeleteAlertDialog
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
    onReorderHabits: () -> Unit,
    onEditHabit: (Long) -> Unit,
    onHabitClick: (Long) -> Unit,
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    LaunchedEffect(Unit) {
        val actionAdd = AppBarAction.add.copy(color = onSurface, onClick = onCreateHabit)
        val actionView = AppBarAction.view.copy(color = onSurface,
            onClick = { viewModel.onEvent(HabitListEvent.ShowViewType(true)) })
        viewModel.initAppBar(
            canNavigateUp = false,
            title = R.string.habit_list_title,
            menuItems = listOf(actionAdd, actionView)
        )
    }

    val view = viewModel.view.collectAsState()
    val state = viewModel.state()

    Box {
        if (state.value.isLoading) LoadingCirclesFullscreen()
        else if (state.value.habits.isEmpty()) EmptyHabitsList()
        if (state.value.isViewTypeVisible)
            ViewBottomSheet(
                view = view.value,
                hideBottomSheet = { viewModel.onEvent(HabitListEvent.ShowViewType(false)) },
                selectViewType = { viewModel.onEvent(HabitListEvent.UpdateView(viewType = it)) },
                selectSorting = { viewModel.onEvent(HabitListEvent.UpdateView(sortBy = it)) },
                selectFilter = { viewModel.onEvent(HabitListEvent.UpdateView(filterBy = it)) },
                reorder = onReorderHabits
            )
        state.value.contextMenuHabitId?.let { id ->
            HabitBottomSheet(
                hideBottomSheet = { viewModel.onEvent(HabitListEvent.UpdateContextMenu(null)) },
                editHabit = { onEditHabit(id) },
                deleteHabit = { viewModel.onEvent(HabitListEvent.ShowDeleteDialog(id)) },
                reorder = onReorderHabits
            )
        }
        Habits(
            state = state,
            view = view.value.viewType.item,
            onHabitClick = { onHabitClick(it.id) },
            onHabitLongClick = { habit -> viewModel.onEvent(HabitListEvent.UpdateContextMenu(habit.id)) },
            onHabitDone = { habit, daysAgo -> viewModel.habitDone(habit, daysAgo) },
            updateState = { entries -> viewModel.onEvent(HabitListEvent.UpdateEntries(entries)) }
        )
    }

    state.value.deleteDialogHabitId?.let { id ->
        DeleteAlertDialog(
            onDismissRequest = { viewModel.onEvent(HabitListEvent.ShowDeleteDialog(null)) },
            onConfirm = {
                viewModel.deleteHabit(id)
                viewModel.onEvent(HabitListEvent.ShowDeleteDialog(null))
                viewModel.onEvent(HabitListEvent.UpdateContextMenu(null))
            })
    }
}

@Composable
fun EmptyHabitsList() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "list of habits is empty")
    }
}

@Composable
private fun Habits(
    state: State<HabitListState>,
    view: ViewType,
    updateState: (Int) -> Unit,
    onHabitClick: (HabitUi) -> Unit = {},
    onHabitLongClick: (HabitUi) -> Unit = {},
    onHabitDone: (HabitUi, Int) -> Unit = { _, _ -> }
) {
    HabitList(
        habits = state.value.habits,
        view = view,
        onDone = onHabitDone,
        onClick = onHabitClick,
        onLongClick = onHabitLongClick,
        updateView = updateState
    )
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitScreenPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme(darkTheme = true) {
        HabitList(habits = habits)
    }
}