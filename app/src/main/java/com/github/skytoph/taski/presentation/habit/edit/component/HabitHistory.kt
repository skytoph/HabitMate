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
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.ui.theme.TaskiTheme
import java.util.Calendar

@Composable
fun HabitHistory(
    habitColor: Color = IconsColors.allColors.first(),
    history: Map<Int, Int> = mapOf(1 to 1, 2 to 1),
    onDayClick: (Int) -> Unit = {},
) {
    val editable = remember { mutableStateOf(false) }
    Column(
        Modifier.background(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(10f)
        )
    ) {
        HabitHistoryGrid(
            habitColor = habitColor,
            history = history,
            isEditable = editable.value,
            onDayClick = onDayClick
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (editable.value)
                Text(
                    text = "tap on the day to change the value",
                    color = MaterialTheme.colorScheme.onBackground
                )
            IconButton(onClick = { editable.value = !editable.value }) {
                Icon(
                    imageVector = if (editable.value) Icons.Outlined.Check else Icons.Default.Edit,
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
    dates: List<DateUi> = emptyList(),
    habitColor: Color = IconsColors.allColors.first(),
    history: Map<Int, Int> = mapOf(1 to 1, 2 to 1),
    isEditable: Boolean = false,
    squareDp: Dp = 26.dp,
    squareOffsetDp: Dp = 1.dp,
    initialOffsetDp: Dp = 4.dp,
    onDayClick: (Int) -> Unit = {}
) {
    val state = remember { mutableStateOf<List<DateUi>>(emptyList()) }

    val heightDp = 7 * squareDp + 6 * squareOffsetDp

    var maxWidth by remember { mutableStateOf(0) }

    val localDensity = LocalDensity.current

    LaunchedEffect(maxWidth) {
        if (maxWidth > 0) {
            val widthDp = with(localDensity) { maxWidth.toDp() }
            val columns =
                (widthDp - initialOffsetDp + 5 * squareDp).div(squareDp + squareOffsetDp).toInt()
            state.value = generateList(columns)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(initialOffsetDp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10f)
            )
            .onGloballyPositioned { coordinates -> maxWidth = coordinates.size.width }
    ) {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(7),
            modifier = Modifier.height(heightDp),
            contentPadding = PaddingValues(2.dp),
            reverseLayout = true,
            verticalArrangement = Arrangement.Bottom
        ) {
            items(state.value) { date ->
                val isSelected = history.contains(date.daysAgo)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(squareDp)
                            .padding(squareOffsetDp)
                            .background(
                                if (isSelected) habitColor else MaterialTheme.colorScheme.onSecondaryContainer,
                                shape = RoundedCornerShape(10)
                            )
                            .clickable {
                                if (isEditable && date.daysAgo > 0) onDayClick(date.daysAgo)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.day,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

        }
    }
}

private fun generateList(columns: Int): List<DateUi> {
    val list = ArrayList<DateUi>(columns)
    val rows = 7

    val daysOffset = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - rows

    for (index in 0 until columns * rows) {
        val calendar = Calendar.getInstance()
        val daysAgo = (rows - index % rows - 1) + index / rows * rows + daysOffset
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        list.add(DateUi(calendar.get(Calendar.DAY_OF_MONTH).toString(), daysAgo))
    }
    return list
}

data class DateUi(val day: String, val daysAgo: Int)

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