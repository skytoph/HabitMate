package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.SportsGymnastics
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.presentation.core.component.LoadingCirclesFullscreen
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.icon.GreenBright
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.presentation.habit.icon.PinkRose
import com.github.skytoph.taski.presentation.habit.list.EntryUi
import com.github.skytoph.taski.presentation.habit.list.HabitListState
import com.github.skytoph.taski.presentation.habit.list.HabitsViewModel
import com.github.skytoph.taski.ui.theme.TaskiTheme


@Composable
fun HabitsScreen(
    viewModel: HabitsViewModel = hiltViewModel(),
    onCreateHabit: () -> Unit,
    onHabitClick: (HabitUi<EntryUi>) -> Unit,
) {
    Habits(
        state = viewModel.state(),
        onCreateHabit = onCreateHabit,
        onHabitClick = onHabitClick,
        onHabitDone = { habit -> viewModel.habitDone(habit) }
    )
}

@Composable
private fun Habits(
    state: State<HabitListState>,
    onCreateHabit: () -> Unit = {},
    onHabitClick: (HabitUi<EntryUi>) -> Unit = {},
    onHabitDone: (HabitUi<EntryUi>) -> Unit = {}
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = onCreateHabit,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = Icons.Default.Add.name)
        }
    }) { paddingValues ->
        if (state.value.isLoading) LoadingCirclesFullscreen()
        else HabitList(
            modifier = Modifier.padding(paddingValues),
            habits = state.value.habits,
            onDoneHabit = onHabitDone,
            onHabitClick = onHabitClick
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitScreenPreview() {
    val habits = listOf(
        HabitUi<EntryUi>(
            0, "dev", 1, Icons.Outlined.Code, GreenBright, emptyList()
        ),
        HabitUi<EntryUi>(
            1, "yoga", 1, Icons.Outlined.SportsGymnastics, PinkRose, emptyList()
        ),
    )

    TaskiTheme(darkTheme = true) {
        Habits(state = remember {
            val habit =
                HabitUi<EntryUi>(0, "dev", 1, Icons.Outlined.Code, IconsColors.allColors.first(), emptyList())
            mutableStateOf(HabitListState(habits = habits, isLoading = false))
        })
    }
}