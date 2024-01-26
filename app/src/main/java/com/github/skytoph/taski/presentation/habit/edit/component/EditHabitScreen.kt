@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.habit.edit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.HabitAppBar
import com.github.skytoph.taski.presentation.core.component.SquareButton
import com.github.skytoph.taski.presentation.core.component.TitleTextField
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.edit.EditHabitEvent
import com.github.skytoph.taski.presentation.habit.edit.EditHabitState
import com.github.skytoph.taski.presentation.habit.edit.EditHabitViewModel
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun EditHabitScreen(
    viewModel: EditHabitViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    onSelectIconClick: () -> Unit
) {
    EditHabit(
        state = viewModel.state(),
        minHeight = TextFieldDefaults.MinHeight,
        navigateUp = navigateUp,
        onSelectIconClick = onSelectIconClick,
        onDeleteClick = { viewModel.onEvent(EditHabitEvent.ShowDialog(true)) },
        onDeleteHabit = { viewModel.deleteHabit() },
        onHideDialog = { viewModel.onEvent(EditHabitEvent.ShowDialog(false)) },
        onSaveHabit = { viewModel.saveHabit() },
        onTypeTitle = { viewModel.onEvent(EditHabitEvent.EditTitle(it)) },
        onDecreaseGoal = { viewModel.onEvent(EditHabitEvent.DecreaseGoal) },
        onIncreaseGoal = { viewModel.onEvent(EditHabitEvent.IncreaseGoal) },
        onDayClick = { viewModel.habitDone(it) },
        onEditHistory = { viewModel.onEvent(EditHabitEvent.EditHistory) }
    )
}

@Composable
private fun EditHabit(
    state: State<EditHabitState>,
    minHeight: Dp = 56.dp,
    navigateUp: () -> Unit = {},
    onSelectIconClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onDeleteHabit: () -> Unit = {},
    onHideDialog: () -> Unit = {},
    onSaveHabit: () -> Unit = {},
    onTypeTitle: (String) -> Unit = {},
    onDecreaseGoal: () -> Unit = {},
    onIncreaseGoal: () -> Unit = {},
    onDayClick: (Int) -> Unit = {},
    onEditHistory: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        HabitAppBar(
            label = "edit habit",
            navigateUp = navigateUp,
            isSaveButtonVisible = true,
            onSaveButtonClick = onSaveHabit,
            isDeleteButtonVisible = true,
            onDeleteButtonClick = onDeleteClick
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
            onIncreaseGoal = onIncreaseGoal
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "history", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        HabitHistory(
            history = state.value.history,
            habitColor = state.value.color,
            onDayClick = onDayClick,
            onEdit = onEditHistory
        )
    }

    if (state.value.isDialogShown)
        DeleteAlertDialog(
            onDismissRequest = onHideDialog,
            onConfirm = {
                onDeleteHabit()
                onHideDialog()
                navigateUp()
            })
}

@Composable
fun EditBaseHabit(
    title: FieldState,
    goal: GoalState,
    icon: ImageVector,
    color: Color,
    onTypeTitle: (String) -> Unit,
    minHeight: Dp,
    onSelectIconClick: () -> Unit,
    onDecreaseGoal: () -> Unit,
    onIncreaseGoal: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        TitleTextField(
            modifier = Modifier.weight(1f),
            value = title.field,
            onValueChange = onTypeTitle
        )
        IconSelector(
            icon = icon,
            color = color,
            size = minHeight,
            onClick = onSelectIconClick
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "goal", style = MaterialTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(4.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(R.string.goal_value, goal.value),
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10)
                )
                .padding(horizontal = 16.dp)
                .height(48.dp)
                .wrapContentHeight()
                .weight(1f),
        )
        SquareButton(
            onClick = onDecreaseGoal,
            icon = Icons.Default.Remove,
            size = 48.dp,
            isEnabled = goal.canBeDecreased
        )
        SquareButton(
            onClick = onIncreaseGoal,
            icon = Icons.Default.Add,
            size = 48.dp,
            isEnabled = goal.canBeIncreased
        )
    }
}

@Composable
fun IconSelector(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    color: Color,
    size: Dp,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = modifier, text = "icon", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(size)
                .background(color = color, shape = RoundedCornerShape(10))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitScreenPreview() {
    TaskiTheme {
        EditHabit(state = remember { mutableStateOf(EditHabitState()) })
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DarkHabitScreenPreview() {
    TaskiTheme(darkTheme = true) {
        EditHabit(state = remember { mutableStateOf(EditHabitState()) })
    }
}