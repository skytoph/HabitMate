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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.HabitAppBar
import com.github.skytoph.taski.presentation.core.component.SaveIconButton
import com.github.skytoph.taski.presentation.core.component.SquareButton
import com.github.skytoph.taski.presentation.core.component.TitleTextField
import com.github.skytoph.taski.presentation.core.preview.HabitsEditableProvider
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.details.components.DeleteAlertDialog
import com.github.skytoph.taski.presentation.habit.edit.EditHabitEvent
import com.github.skytoph.taski.presentation.habit.edit.EditHabitState
import com.github.skytoph.taski.presentation.habit.edit.EditHabitViewModel
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun EditHabitScreen(
    viewModel: EditHabitViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    onSelectIconClick: () -> Unit
) {

    val iconState = remember { viewModel.iconState() }
    LaunchedEffect(iconState.value) {
        viewModel.onEvent(EditHabitEvent.UpdateIcon(iconState.value.icon, iconState.value.color))
    }
    val validated = viewModel.state().value.isValidated
    LaunchedEffect(validated) {
        if (validated) viewModel.saveHabit(navigateUp = navigateUp)
    }

    EditHabit(
        state = viewModel.state(),
        minHeight = TextFieldDefaults.MinHeight,
        navigateUp = navigateUp,
        onSelectIconClick = onSelectIconClick,
        onSaveHabit = { viewModel.validate() },
        onTypeTitle = { viewModel.onEvent(EditHabitEvent.EditTitle(it)) },
        onDecreaseGoal = { viewModel.onEvent(EditHabitEvent.DecreaseGoal) },
        onIncreaseGoal = { viewModel.onEvent(EditHabitEvent.IncreaseGoal) },
    )
}

@Composable
private fun EditHabit(
    state: State<EditHabitState>,
    minHeight: Dp = 56.dp,
    navigateUp: () -> Unit = {},
    onSelectIconClick: () -> Unit = {},
    onDeleteHabit: () -> Unit = {},
    onHideDialog: () -> Unit = {},
    onSaveHabit: () -> Unit = {},
    onTypeTitle: (String) -> Unit = {},
    onDecreaseGoal: () -> Unit = {},
    onIncreaseGoal: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        HabitAppBar(
            label = stringResource(R.string.edit_habit),
            navigateUp = { navigateUp() },
            menuItems = listOf(SaveIconButton(MaterialTheme.colorScheme.onSurface, onSaveHabit)),
            isDropDownMenu = false
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
        Spacer(modifier = Modifier.height(16.dp))
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
            onValueChange = onTypeTitle,
            error = title.error?.getString(LocalContext.current)
        )
        IconSelector(
            icon = icon,
            color = color,
            size = minHeight,
            onClick = onSelectIconClick
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = stringResource(R.string.goal_label), style = MaterialTheme.typography.bodyMedium)
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
        Text(
            modifier = modifier,
            text = stringResource(R.string.icon_label),
            style = MaterialTheme.typography.bodyMedium
        )
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
fun HabitScreenPreview(@PreviewParameter(HabitsEditableProvider::class) entries: List<EditableHistoryUi>) {
    HabitMateTheme(darkTheme = false) {
        EditHabit(
            state = remember { mutableStateOf(EditHabitState()) }
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DarkHabitScreenPreview(@PreviewParameter(HabitsEditableProvider::class) entries: List<EditableHistoryUi>) {
    HabitMateTheme(darkTheme = true) {
        EditHabit(
            state = remember { mutableStateOf(EditHabitState()) }
        )
    }
}