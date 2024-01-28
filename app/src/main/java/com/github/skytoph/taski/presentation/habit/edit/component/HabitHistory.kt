package com.github.skytoph.taski.presentation.habit.edit.component

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.github.skytoph.taski.presentation.core.component.getLocale
import com.github.skytoph.taski.presentation.core.fadingEdge
import com.github.skytoph.taski.presentation.core.habitColor
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.edit.HistoryState
import com.github.skytoph.taski.presentation.habit.edit.MonthUi
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.ui.theme.TaskiTheme
import kotlin.math.ceil

@Composable
fun HabitHistory(
    history: HistoryState = HistoryState(),
    habitColor: Color = IconsColors.allColors.first(),
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
            onDayClick = onDayClick,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (history.isEditable)
                Text(
                    text = "tap on the day to change the value",
                    color = MaterialTheme.colorScheme.onBackground
                )
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = if (history.isEditable) Icons.Outlined.Check else Icons.Default.Edit,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitHistoryGrid(
    history: HistoryState = HistoryState(),
    habitColor: Color = IconsColors.allColors.first(),
    onDayClick: (Int) -> Unit = {},
    squareDp: Dp = 32.dp,
    squareOffsetDp: Dp = 1.dp,
    initialOffsetDp: Dp = 4.dp,
) {
    val density = LocalDensity.current

    val heightDp = 8 * squareDp
    val widthDp = ceil(history.entries.size.toFloat() / 7F).toInt() * squareDp

    val fadingBrush = Brush.horizontalGradient(
        0f to Color.Transparent,
        0.1f to Color.Black,
        endX = with(density) { (40.dp).toPx() }
    )

    Box(
        modifier = Modifier
            .padding(initialOffsetDp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10f)
            )
    ) {
        LazyHorizontalStaggeredGrid(
            rows = StaggeredGridCells.Fixed(8),
            modifier = Modifier
                .height(heightDp)
                .padding(start = initialOffsetDp)
                .fadingEdge(fadingBrush),
            contentPadding = PaddingValues(2.dp),
            reverseLayout = true,
        ) {
            item {
                MonthsLabels(history.months, squareDp, widthDp, squareOffsetDp)
            }
            items(history.entries) { entry ->
                DailyEntry(
                    entry,
                    history.isEditable,
                    onDayClick,
                    squareDp,
                    squareOffsetDp,
                    habitColor
                )
            }
        }
    }
}

@Composable
private fun DailyEntry(
    entry: EntryEditableUi,
    isEditable: Boolean,
    onDayClick: (Int) -> Unit,
    size: Dp,
    padding: Dp,
    entryColor: Color
) {
    Box(
        modifier = Modifier
            .size(size)
            .padding(padding)
            .background(
                habitColor(
                    entry.colorPercent,
                    MaterialTheme.colorScheme.onSecondaryContainer,
                    entryColor
                ),
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

@Composable
private fun MonthsLabels(
    months: List<MonthUi>,
    entrySize: Dp,
    widthDp: Dp,
    padding: Dp,
) {
    LazyRow(
        userScrollEnabled = false,
        reverseLayout = true,
        modifier = Modifier
            .height(entrySize)
            .width(widthDp)
    ) {
        items(months, key = { it.timestamp }) { month ->
            Box(
                modifier = Modifier
                    .width(entrySize.times(month.weeks))
                    .height(entrySize)
                    .padding(padding),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = month.getDisplayName(getLocale()),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = month.alignment
                )
            }
        }
    }
}

val months = (1..12).map { MonthUi(weeks = if (it == 1) 2 else 4) }
val history = (1..298).map { EntryEditableUi((it % 30).toString(), 0F, it) }

@Composable
@Preview
fun DarkCalendarEditableGridPreview() {
    TaskiTheme(darkTheme = true) {
        Surface(modifier = Modifier.padding(16.dp)) {
            HabitHistory(HistoryState(history, months))
        }
    }
}