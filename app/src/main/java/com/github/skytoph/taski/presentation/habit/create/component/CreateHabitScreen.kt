package com.github.skytoph.taski.presentation.habit.create.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.HabitAppBar
import com.github.skytoph.taski.presentation.core.component.SaveIconButton
import com.github.skytoph.taski.presentation.habit.create.CreateHabitEvent
import com.github.skytoph.taski.presentation.habit.create.CreateHabitState
import com.github.skytoph.taski.presentation.habit.create.CreateHabitViewModel
import com.github.skytoph.taski.presentation.habit.edit.component.EditBaseHabit
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun CreateHabitScreen(
    viewModel: CreateHabitViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    onSelectIconClick: () -> Unit
) {
    val iconState = remember { viewModel.iconState() }
    LaunchedEffect(iconState.value) {
        viewModel.onEvent(CreateHabitEvent.UpdateIcon(iconState.value.icon, iconState.value.color))
    }

    CreateHabit(
        state = viewModel.state(),
        minHeight = TextFieldDefaults.MinHeight,
        navigateUp = navigateUp,
        onSelectIconClick = onSelectIconClick,
        onSaveHabit = { viewModel.saveHabit(); navigateUp() },
        onTypeTitle = { viewModel.onEvent(CreateHabitEvent.EditTitle(it)) },
        onDecreaseGoal = { viewModel.onEvent(CreateHabitEvent.DecreaseGoal) },
        onIncreaseGoal = { viewModel.onEvent(CreateHabitEvent.IncreaseGoal) })
}

@Composable
private fun CreateHabit(
    state: State<CreateHabitState>,
    minHeight: Dp = 56.dp,
    navigateUp: () -> Unit = {},
    onSelectIconClick: () -> Unit = {},
    onSaveHabit: () -> Unit = {},
    onTypeTitle: (String) -> Unit = {},
    onDecreaseGoal: () -> Unit = {},
    onIncreaseGoal: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        HabitAppBar(
            label = stringResource(R.string.create_new_habit),
            navigateUp = navigateUp,
            actionButtons = listOf(SaveIconButton(MaterialTheme.colorScheme.onSurface, onSaveHabit))
        )
        EditBaseHabit(
            title = state.value.title,
            goal = state.value.goal,
            icon = state.value.icon,
            color = state.value.color,
            onTypeTitle = onTypeTitle,
            minHeight = minHeight,
            onSelectIconClick = onSelectIconClick,
            onDecreaseGoal = onDecreaseGoal,
            onIncreaseGoal = onIncreaseGoal,
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitScreenPreview() {
    TaskiTheme {
        CreateHabit(state = remember { mutableStateOf(CreateHabitState()) })
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DarkHabitScreenPreview() {
    TaskiTheme(darkTheme = true) {
        CreateHabit(state = remember { mutableStateOf(CreateHabitState()) })
    }
}