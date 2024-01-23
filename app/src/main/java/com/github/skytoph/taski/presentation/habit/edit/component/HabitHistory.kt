package com.github.skytoph.taski.presentation.habit.edit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.github.skytoph.taski.presentation.core.habitColor
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun HabitHistory(
    history: List<EntryEditableUi> = emptyList(),
    habitColor: Color = IconsColors.allColors.first(),
    isEditable: Boolean = false,
    onEdit: () -> Unit = {},
    onDayClick: (Int) -> Unit = {},
) {
    Column(
        Modifier.background(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(10f)
        )
    ) {
        HabitHistoryGrid(
            history = history,
            habitColor = habitColor,
            isEditable = isEditable,
            onDayClick = onDayClick,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isEditable)
                Text(
                    text = "tap on the day to change the value",
                    color = MaterialTheme.colorScheme.onBackground
                )
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = if (isEditable) Icons.Outlined.Check else Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(30)
                        )
                        .padding(4.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun HabitHistoryGrid(
    history: List<EntryEditableUi> = emptyList(),
    habitColor: Color = IconsColors.allColors.first(),
    isEditable: Boolean = false,
    onDayClick: (Int) -> Unit = {},
    squareDp: Dp = 26.dp,
    squareOffsetDp: Dp = 1.dp,
    initialOffsetDp: Dp = 4.dp,
) {
    val heightDp = 7 * squareDp + 6 * squareOffsetDp

    val defaultColor = MaterialTheme.colorScheme.onSecondaryContainer

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(initialOffsetDp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10f)
            )
    ) {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(7),
            modifier = Modifier.height(heightDp),
            contentPadding = PaddingValues(2.dp),
            reverseLayout = true,
            verticalArrangement = Arrangement.Bottom
        ) {
            items(history) { entry ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(squareDp)
                            .padding(squareOffsetDp)
                            .background(
                                habitColor(entry.colorPercent, defaultColor, habitColor),
                                shape = RoundedCornerShape(10)
                            )
                            .clickable {
                                if (isEditable && entry.daysAgo >= 0)
                                    onDayClick(entry.daysAgo)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = entry.day,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

        }
    }
}

@Composable
@Preview
fun CalendarEditableGridPreview() {
    TaskiTheme {
        HabitHistory()
    }
}

@Composable
@Preview
fun DarkCalendarEditableGridPreview() {
    TaskiTheme(darkTheme = true) {
        HabitHistory()
    }
}