package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.presentation.core.component.AppBarAction
import com.github.skytoph.taski.presentation.core.component.LoadingCirclesFullscreen
import com.github.skytoph.taski.presentation.core.preview.HabitsProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HabitListState
import com.github.skytoph.taski.presentation.habit.list.HabitsViewModel
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme


@Composable
fun HabitsScreen(
    viewModel: HabitsViewModel = hiltViewModel(),
    onCreateHabit: () -> Unit,
    onHabitClick: (HabitUi) -> Unit,
) {
    val onSecondaryContainer = MaterialTheme.colorScheme.onSecondaryContainer
    LaunchedEffect(Unit) {
        viewModel.init(onSecondaryContainer)
    }

    val onSurface = MaterialTheme.colorScheme.onSurface
    LaunchedEffect(Unit) {
        val actionAdd = AppBarAction.add.copy(color = onSurface, onClick = onCreateHabit)
        viewModel.initAppBar(canNavigateUp = false, menuItems = listOf(actionAdd))
    }

    Habits(
        state = viewModel.state(),
        onHabitClick = onHabitClick,
        onHabitDone = { habit -> viewModel.habitDone(habit) }
    )
}

@Composable
private fun Habits(
    state: State<HabitListState>,
    onHabitClick: (HabitUi) -> Unit = {},
    onHabitDone: (HabitUi) -> Unit = {}
) {
    if (state.value.isLoading) LoadingCirclesFullscreen()
    else HabitList(
        habits = state.value.habits,
        onDoneHabit = onHabitDone,
        onHabitClick = onHabitClick
    )
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitScreenPreview(@PreviewParameter(HabitsProvider::class) habits: List<HabitWithHistoryUi<HistoryUi>>) {
    HabitMateTheme(darkTheme = true) {
        Habits(state = remember {
            mutableStateOf(HabitListState(habits = habits, isLoading = false))
        })
    }
}