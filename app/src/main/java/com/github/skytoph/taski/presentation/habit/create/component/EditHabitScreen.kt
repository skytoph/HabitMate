@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.habit.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.TextField
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
import com.github.skytoph.taski.presentation.core.component.LoadingCirclesFullscreen
import com.github.skytoph.taski.presentation.core.component.SquareButton
import com.github.skytoph.taski.presentation.habit.create.EditHabitEvent
import com.github.skytoph.taski.presentation.habit.create.EditHabitState
import com.github.skytoph.taski.presentation.habit.create.EditHabitViewModel
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun EditHabitScreen(
    viewModel: EditHabitViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    onSelectIconClick: () -> Unit
) {
    EditHabit(
        viewModel.state(),
        TextFieldDefaults.MinHeight,
        navigateUp,
        onSelectIconClick,
        { viewModel.saveHabit() },
        { viewModel.onEvent(EditHabitEvent.EditTitle(it)) },
        { viewModel.onEvent(EditHabitEvent.DecreaseGoal) },
        { viewModel.onEvent(EditHabitEvent.IncreaseGoal) })
}

@Composable
private fun EditHabit(
    state: State<EditHabitState>,
    minHeight: Dp = 56.dp,
    navigateUp: () -> Unit = {},
    onSelectIconClick: () -> Unit = {},
    onSaveHabit: () -> Unit = {},
    onTypeTitle: (String) -> Unit = {},
    onDecreaseGoal: () -> Unit = {},
    onIncreaseGoal: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        if (state.value.isLoading) LoadingCirclesFullscreen()
        HabitAppBar(
            label = if (state.value.isNewHabit) "new habit" else "edit habit",
            navigateUp = navigateUp,
            isSaveButtonVisible = true,
            onSaveButtonClick = onSaveHabit
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TitleTextField(
                modifier = Modifier.weight(1f),
                value = state.value.title.field,
                onValueChange = onTypeTitle
            )
            IconSelector(
                icon = state.value.icon,
                color = state.value.color,
                size = minHeight,
                onClick = onSelectIconClick
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "goal", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.goal_value, state.value.goal.value),
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
                isEnabled = state.value.goal.canBeDecreased
            )
            SquareButton(
                onClick = onIncreaseGoal,
                icon = Icons.Default.Add,
                size = 48.dp,
                isEnabled = state.value.goal.canBeIncreased
            )
        }
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
private fun TitleTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(text = "habit", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            shape = RoundedCornerShape(10),
        )
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