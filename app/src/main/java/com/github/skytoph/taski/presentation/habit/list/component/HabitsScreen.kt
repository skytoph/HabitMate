@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.AppBarAction
import com.github.skytoph.taski.presentation.core.component.EmptyScreen
import com.github.skytoph.taski.presentation.core.component.LoadingFullscreen
import com.github.skytoph.taski.presentation.core.preview.HabitsProvider
import com.github.skytoph.taski.presentation.habit.HabitScreens
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
    onSettingsClick: () -> Unit,
    onReorderHabits: () -> Unit,
    onEditHabit: (Long) -> Unit,
    onHabitClick: (Long) -> Unit,
    removeHabitFromState: (String) -> Unit,
    deleteState: State<Long?>,
    archiveState: State<Long?>,
) {
    val onSurface = MaterialTheme.colorScheme.onBackground
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val actionAdd = AppBarAction.add.copy(color = onSurface, onClick = onCreateHabit)
        val actionView = AppBarAction.view.copy(color = onSurface,
            onClick = { viewModel.onEvent(HabitListEvent.ShowViewType(true)) })
        val actionSettings =
            AppBarAction.settings.copy(color = onSurface, onClick = onSettingsClick)
        viewModel.initAppBar(
            canNavigateUp = false,
            title = R.string.habit_list_title,
            menuItems = listOf(actionAdd),
            dropDownItems = listOf(actionView, actionSettings)
        )
    }
    val messageDelete = stringResource(R.string.message_habit_deleted)
    val messageArchive = stringResource(R.string.message_habit_archived)

    LaunchedEffect(deleteState.value, archiveState.value) {
        deleteState.value?.let { id ->
            removeHabitFromState(HabitScreens.HabitList.keyDelete)
            viewModel.deleteHabit(id, messageDelete, context)
        }
        archiveState.value?.let { id ->
            removeHabitFromState(HabitScreens.HabitList.keyArchive)
            viewModel.archiveHabit(id, messageArchive)
        }
    }

    val settings = viewModel.view.collectAsState()
    val state = viewModel.habitsState()

    if (state.value.isLoading)
        LoadingFullscreen()
    else if (state.value.habits.isEmpty())
        EmptyScreen(
            title = stringResource(R.string.list_of_habits_is_empty_label),
            icon = ImageVector.vectorResource(R.drawable.sparkles)
        )
    else
        Habits(
            state = state,
            view = settings.value.view.viewType.data,
            onHabitClick = { onHabitClick(it.id) },
            onHabitLongClick = { habit -> viewModel.onEvent(HabitListEvent.UpdateContextMenu(habit.id)) },
            onHabitDone = { habit, daysAgo -> viewModel.habitDone(habit, daysAgo) },
            updateState = { entries -> viewModel.onEvent(HabitListEvent.UpdateEntries(entries)) }
        )

    if (state.value.isViewTypeVisible)
        ViewBottomSheet(
            view = settings.value.view,
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
            archiveHabit = { viewModel.onEvent(HabitListEvent.ShowArchiveDialog(id)) },
            reorder = onReorderHabits
        )
    }

    state.value.deleteDialogHabitId?.let { id ->
        val message = stringResource(R.string.message_habit_deleted)
        DeleteDialog(
            onDismissRequest = { viewModel.onEvent(HabitListEvent.ShowDeleteDialog(null)) },
            onConfirm = {
                viewModel.deleteHabit(id, message, context)
                viewModel.onEvent(HabitListEvent.ShowDeleteDialog(null))
                viewModel.onEvent(HabitListEvent.UpdateContextMenu(null))
            })
    }

    state.value.archiveDialogHabitId?.let { id ->
        val message = stringResource(R.string.message_habit_archived)
        ArchiveDialog(
            onDismissRequest = { viewModel.onEvent(HabitListEvent.ShowArchiveDialog(null)) },
            onConfirm = {
                viewModel.archiveHabit(id, message)
                viewModel.onEvent(HabitListEvent.ShowArchiveDialog(null))
                viewModel.onEvent(HabitListEvent.UpdateContextMenu(null))
            })
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