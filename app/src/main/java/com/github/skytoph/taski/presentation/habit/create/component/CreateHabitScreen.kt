package com.github.skytoph.taski.presentation.habit.create.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.AppBarAction
import com.github.skytoph.taski.presentation.habit.create.CreateHabitEvent
import com.github.skytoph.taski.presentation.habit.create.CreateHabitState
import com.github.skytoph.taski.presentation.habit.create.CreateHabitViewModel
import com.github.skytoph.taski.presentation.habit.edit.component.EditBaseHabit
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyCustomType
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun CreateHabitScreen(
    viewModel: CreateHabitViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    onSelectIconClick: () -> Unit
) {
    val context = LocalContext.current

    val iconState = remember { viewModel.iconState() }
    LaunchedEffect(iconState.value) {
        viewModel.onEvent(CreateHabitEvent.UpdateIcon(iconState.value.icon, iconState.value.color))
    }

    val validated = viewModel.state().value.isValidated
    LaunchedEffect(validated) {
        if (validated) viewModel.saveHabit(navigateUp, context)
    }

    val menuItemColor = MaterialTheme.colorScheme.onSurface
    LaunchedEffect(Unit) {
        val actionSave =
            AppBarAction.save.copy(color = menuItemColor, onClick = { viewModel.validate() })
        viewModel.initAppBar(title = R.string.create_new_habit, menuItems = listOf(actionSave))
    }

    CreateHabit(
        state = viewModel.state(),
        onSelectIconClick = onSelectIconClick,
        onTypeTitle = { viewModel.onEvent(CreateHabitEvent.EditTitle(it)) },
        onDecreaseGoal = { viewModel.onEvent(CreateHabitEvent.DecreaseGoal) },
        onIncreaseGoal = { viewModel.onEvent(CreateHabitEvent.IncreaseGoal) },
        expandFrequency = { viewModel.onEvent(CreateHabitEvent.ExpandFrequency) },
        increaseTimes = { viewModel.onEvent(CreateHabitEvent.IncreaseFrequencyTimes) },
        decreaseTimes = { viewModel.onEvent(CreateHabitEvent.DecreaseFrequencyTimes) },
        increaseType = { viewModel.onEvent(CreateHabitEvent.IncreaseFrequencyType) },
        decreaseType = { viewModel.onEvent(CreateHabitEvent.DecreaseFrequencyType) },
        selectType = { viewModel.onEvent(CreateHabitEvent.SelectFrequency(it)) },
        selectDay = { viewModel.onEvent(CreateHabitEvent.SelectDay(it)) },
        selectCustomType = { viewModel.onEvent(CreateHabitEvent.SelectCustomType(it)) },
        expandType = { viewModel.onEvent(CreateHabitEvent.ExpandCustomType) })
}

@Composable
private fun CreateHabit(
    state: State<CreateHabitState>,
    onSelectIconClick: () -> Unit = {},
    onTypeTitle: (String) -> Unit = {},
    onDecreaseGoal: () -> Unit = {},
    onIncreaseGoal: () -> Unit = {},
    expandFrequency: () -> Unit = {},
    increaseTimes: () -> Unit = {},
    decreaseTimes: () -> Unit = {},
    increaseType: () -> Unit = {},
    decreaseType: () -> Unit = {},
    selectType: (FrequencyUi) -> Unit = {},
    selectDay: (Int) -> Unit = {},
    selectCustomType: (FrequencyCustomType) -> Unit = {},
    expandType: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        EditBaseHabit(
            title = state.value.title,
            goal = state.value.goal,
            icon = state.value.icon,
            color = state.value.color,
            onTypeTitle = onTypeTitle,
            onSelectIconClick = onSelectIconClick,
            onDecreaseGoal = onDecreaseGoal,
            onIncreaseGoal = onIncreaseGoal,
            frequency = state.value.frequencyState,
            isFrequencyExpanded = state.value.isFrequencyExpanded,
            expandFrequency = expandFrequency,
            increaseTimes = increaseTimes,
            decreaseTimes = decreaseTimes,
            increaseType = increaseType,
            decreaseType = decreaseType,
            selectType = selectType,
            selectDay = selectDay,
            selectCustomType = selectCustomType,
            expandType = expandType,
            typeExpanded = state.value.isCustomTypeExpanded
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitScreenPreview() {
    HabitMateTheme {
        CreateHabit(state = remember { mutableStateOf(CreateHabitState()) })
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DarkHabitScreenPreview() {
    HabitMateTheme(darkTheme = true) {
        CreateHabit(state = remember { mutableStateOf(CreateHabitState()) })
    }
}