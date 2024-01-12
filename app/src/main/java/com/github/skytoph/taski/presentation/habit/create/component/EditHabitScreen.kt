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
import com.github.skytoph.taski.presentation.habit.create.EditHabitEvent
import com.github.skytoph.taski.presentation.habit.create.EditHabitViewModel
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun EditHabitScreen(
    viewModel: EditHabitViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    onSelectIconClick: () -> Unit
) {
    val state = viewModel.state()
    val minHeight = TextFieldDefaults.MinHeight

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        HabitAppBar(
            label = "new habit",
            navigateUp = navigateUp,
            isSaveButtonVisible = true,
            onSaveButtonClick = { viewModel.saveHabit() })
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TitleTextField(
                modifier = Modifier.weight(1f),
                value = state.value.title.field,
                onValueChange = { viewModel.onEvent(EditHabitEvent.EditTitle(it)) })
            IconSelector(
                icon = state.value.icon,
                color = state.value.color,
                size = minHeight,
                onClick = onSelectIconClick
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "goal")
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.goal_value, state.value.goal.value),
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10)
                    )
                    .padding(horizontal = 16.dp)
                    .height(minHeight)
                    .wrapContentHeight()
                    .weight(1f),
            )
            SquareButton(
                onClick = { viewModel.onEvent(EditHabitEvent.DecreaseGoal) },
                icon = Icons.Default.Remove,
                size = minHeight,
                isEnabled = state.value.goal.canBeDecreased
            )
            SquareButton(
                onClick = { viewModel.onEvent(EditHabitEvent.IncreaseGoal) },
                icon = Icons.Default.Add,
                size = minHeight,
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
        Text(modifier = modifier, text = "icon")
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(size)
                .background(
                    color = color,
                    shape = RoundedCornerShape(10)
                )
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
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant

    Column(modifier = modifier) {
        Text(text = "habit")
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
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
        EditHabitScreen(navigateUp = {}, viewModel = hiltViewModel(), onSelectIconClick = {})
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DarkHabitScreenPreview() {
    TaskiTheme(darkTheme = true) {
        EditHabitScreen(navigateUp = {}, viewModel = hiltViewModel(), onSelectIconClick = {})
    }
}