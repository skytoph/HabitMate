package com.github.skytoph.taski.presentation.habit.edit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.AppBarAction
import com.github.skytoph.taski.presentation.core.component.SquareButton
import com.github.skytoph.taski.presentation.core.component.TitleTextField
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.edit.EditHabitEvent
import com.github.skytoph.taski.presentation.habit.edit.EditHabitState
import com.github.skytoph.taski.presentation.habit.edit.EditHabitViewModel
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun EditHabitScreen(
    viewModel: EditHabitViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    onSelectIconClick: () -> Unit
) {
    val context = LocalContext.current

    val actionColor = MaterialTheme.colorScheme.onSurface
    LaunchedEffect(viewModel.state().value.isLoading) {
        if (viewModel.state().value.isLoading) return@LaunchedEffect
        val actionSave =
            AppBarAction.save.copy(color = actionColor, onClick = { viewModel.validate() })
        viewModel.initAppBar(
            title = R.string.edit_habit,
            menuItems = listOf(actionSave)
        )
    }

    val iconState = remember { viewModel.iconState() }
    LaunchedEffect(iconState.value) {
        viewModel.onEvent(EditHabitEvent.UpdateIcon(iconState.value.icon, iconState.value.color))
    }
    val validated = viewModel.state().value.isValidated
    LaunchedEffect(validated) {
        if (validated) viewModel.saveHabit(navigateUp = navigateUp, context = context)
    }

    EditHabit(
        state = viewModel.state(),
        onSelectIconClick = onSelectIconClick,
        onTypeTitle = { viewModel.onEvent(EditHabitEvent.EditTitle(it)) },
        onDecreaseGoal = { viewModel.onEvent(EditHabitEvent.DecreaseGoal) },
        onIncreaseGoal = { viewModel.onEvent(EditHabitEvent.IncreaseGoal) },
    )
}

@Composable
private fun EditHabit(
    state: State<EditHabitState>,
    onSelectIconClick: () -> Unit = {},
    onTypeTitle: (String) -> Unit = {},
    onDecreaseGoal: () -> Unit = {},
    onIncreaseGoal: () -> Unit = {},
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
    )
}

@Composable
fun EditBaseHabit(
    title: FieldState,
    goal: GoalState,
    icon: IconResource,
    color: Color,
    onTypeTitle: (String) -> Unit,
    onSelectIconClick: () -> Unit,
    onDecreaseGoal: () -> Unit,
    onIncreaseGoal: () -> Unit,
    minHeight: Dp = 48.dp
) {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TitleTextField(
                modifier = Modifier.weight(1f),
                value = title.field,
                onValueChange = onTypeTitle,
                error = title.error?.getString(LocalContext.current),
                height = minHeight
            )
            IconSelector(
                icon = icon,
                color = color,
                size = minHeight,
                onClick = onSelectIconClick
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.goal_label),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.goal_value, goal.value),
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .padding(horizontal = 16.dp)
                    .height(minHeight)
                    .wrapContentHeight()
                    .weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            SquareButton(
                onClick = onDecreaseGoal,
                icon = Icons.Default.Remove,
                size = minHeight,
                isEnabled = goal.canBeDecreased
            )
            SquareButton(
                onClick = onIncreaseGoal,
                icon = Icons.Default.Add,
                size = minHeight,
                isEnabled = goal.canBeIncreased
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "frequency",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        EditFrequency(minHeight = minHeight)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun IconSelector(
    modifier: Modifier = Modifier,
    icon: IconResource,
    color: Color,
    size: Dp,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = modifier,
            text = stringResource(R.string.icon_label),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(size)
                .background(color = color, shape = MaterialTheme.shapes.extraSmall)
        ) {
            Icon(
                imageVector = icon.vector(context),
                contentDescription = icon.name(context.resources),
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
@Preview
private fun DarkHabitScreenPreview() {
    HabitMateTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            EditHabit(
                state = remember { mutableStateOf(EditHabitState()) }
            )
        }
    }
}