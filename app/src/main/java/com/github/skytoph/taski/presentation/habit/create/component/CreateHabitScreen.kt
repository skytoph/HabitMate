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
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.github.skytoph.taski.R
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun CreateHabitScreen(onCreateHabitClick: () -> Unit) {
    val state = HabitState("yoga", 1, Icons.Filled.AcUnit, Color.Blue)
    val minHeight = TextFieldDefaults.MinHeight

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCreateHabitClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIos,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "new habit",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onCreateHabitClick) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TitleTextField(modifier = Modifier.weight(1f), value = state.title)
            IconPicker(icon = state.icon, color = state.color, size = minHeight) {}
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "goal")
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.goal_value, state.goal),
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
            SquareButton({ /* todo state.goal--*/ }, "-", minHeight)
            SquareButton({ /* todo state.goal++*/ }, "+", minHeight)
        }
    }
}

@Composable
private fun SquareButton(onClick: () -> Unit, label: String, minHeight: Dp) {
    TextButton(
        onClick = onClick,
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
        modifier = Modifier.size(minHeight)
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onTertiary)
    }
}

@Composable
fun IconPicker(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    color: Color,
    size: Dp,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = modifier, text = "icon")
        IconButton(
            onClick = { /*TODO*/ }, modifier = Modifier
                .size(size)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10)
                )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = color
            )
        }
    }
}

@Composable
private fun TitleTextField(modifier: Modifier = Modifier, value: String) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant

    Column(modifier = modifier) {
        Text(text = "habit")
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = value,
            onValueChange = {},
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            shape = RoundedCornerShape(10),
        )
    }
}

data class HabitState(val title: String, val goal: Int, val icon: ImageVector, val color: Color)

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitScreenPreview() {
    TaskiTheme {
        CreateHabitScreen({})
    }
}