@file:OptIn(ExperimentalMaterial3Api::class)

package com.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skytoph.taski.R
import com.skytoph.taski.core.datastore.settings.ViewType
import com.skytoph.taski.presentation.core.component.AllowCrashlyticsDialog
import com.skytoph.taski.presentation.core.component.AppBarAction
import com.skytoph.taski.presentation.core.component.ArchiveDialog
import com.skytoph.taski.presentation.core.component.ButtonWithIconOnBackground
import com.skytoph.taski.presentation.core.component.DeleteDialog
import com.skytoph.taski.presentation.core.component.EmptyScreen
import com.skytoph.taski.presentation.core.component.LoadingFullscreen
import com.skytoph.taski.presentation.core.preview.HabitsProvider
import com.skytoph.taski.presentation.habit.HabitScreens
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.skytoph.taski.presentation.habit.list.HabitListEvent
import com.skytoph.taski.presentation.habit.list.HabitListState
import com.skytoph.taski.presentation.habit.list.HabitsViewModel
import com.skytoph.taski.presentation.habit.list.HistoryUi
import com.skytoph.taski.ui.theme.HabitMateTheme

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
    val state = viewModel.habitsState()
    val onBackground = MaterialTheme.colorScheme.onBackground
    val context = LocalContext.current
    val actionAdd = AppBarAction.add.copy(color = onBackground, onClick = onCreateHabit)
    LaunchedEffect(state.value.habits.isEmpty(), MaterialTheme.colorScheme.onBackground) {
        viewModel.initAppBar(
            canNavigateUp = false,
            title = R.string.habit_list_title,
            menuItems = if (state.value.habits.isEmpty()) emptyList() else listOf(actionAdd),
            dropDownItems = listOf(
                AppBarAction.view.copy(color = onBackground,
                    onClick = { viewModel.onEvent(HabitListEvent.ShowViewType(true)) }),
                AppBarAction.settings.copy(color = onBackground, onClick = onSettingsClick)
            )
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

    if (state.value.isLoading)
        LoadingFullscreen()
    else if (state.value.habits.isEmpty())
        EmptyScreen(
            title = stringResource(R.string.list_of_habits_is_empty_create_label),
            icon = ImageVector.vectorResource(R.drawable.sparkles_large),
            button = {
                ButtonWithIconOnBackground(
                    modifier = Modifier.padding(top = 4.dp),
                    onClick = onCreateHabit,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    title = stringResource(R.string.action_create),
                    icon = Icons.Default.Add,
                    color = Color.White
                )
            }
        )
    else
        Habits(
            state = state,
            view = settings.value.view.viewType,
            updateState = { entries -> viewModel.onEvent(HabitListEvent.UpdateEntries(entries)) },
            onHabitClick = { onHabitClick(it.id) },
            onHabitLongClick = { habit -> viewModel.onEvent(HabitListEvent.UpdateContextMenu(habit.id)) },
            onHabitDone = { habit, daysAgo -> viewModel.habitDone(habit, daysAgo) },
        )

    if (state.value.isCrashlyticsItemShown)
        AllowCrashlyticsDialog(
            onDismissRequest = { viewModel.onEvent(HabitListEvent.AllowCrashlytics(false)) },
            onConfirm = { viewModel.onEvent(HabitListEvent.AllowCrashlytics(true)) },
        )

    if (state.value.isViewTypeVisible)
        ViewBottomSheet(
            view = settings.value.view,
            hideBottomSheet = { viewModel.onEvent(HabitListEvent.ShowViewType(false)) },
            selectViewType = { viewModel.onEvent(HabitListEvent.UpdateView(viewType = it)) },
            selectSorting = { viewModel.onEvent(HabitListEvent.UpdateView(sortBy = it)) },
            selectFilter = { viewModel.onEvent(HabitListEvent.UpdateView(filterBy = it)) },
            showTodayHabitsOnly = { viewModel.onEvent(HabitListEvent.UpdateView(showTodayHabitsOnly = it)) },
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
            },
            text = stringResource(R.string.delete_habit_confirmation_dialog_description),
            title = stringResource(R.string.delete_habit_confirmation_dialog_title)
        )
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
    onHabitDone: (HabitUi, Int) -> Unit = { _, _ -> },
) {
    HabitList(
        view = view,
        habits = state.value.habits,
        onDone = onHabitDone,
        onClick = onHabitClick,
        onLongClick = onHabitLongClick,
        updateView = updateState,
    )
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitScreenPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme(darkTheme = true) {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) { HabitList(habits = habits) }
    }
}