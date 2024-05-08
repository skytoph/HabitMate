@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)

package com.github.skytoph.taski.presentation.habit.details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.color.contrastColor
import com.github.skytoph.taski.presentation.core.component.getLocale
import com.github.skytoph.taski.presentation.core.fadingEdge
import com.github.skytoph.taski.presentation.core.format.getWeekDisplayName
import com.github.skytoph.taski.presentation.core.leftFadingEdge
import com.github.skytoph.taski.presentation.core.preview.HabitsEditableProvider
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.edit.MonthUi
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.ui.theme.HabitMateTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun HabitHistory(
    entries: Flow<PagingData<EditableHistoryUi>>,
    goal: Int = 1,
    isEditable: Boolean = false,
    isEditButtonVisible: Boolean = true,
    habitColor: Color = IconsColors.Default,
    onEdit: () -> Unit = {},
    onDayClick: (Int) -> Unit = {},
) {
    Column(
        Modifier
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(10f)
            )
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        HabitHistoryGrid(
            entries = entries,
            goal = goal,
            isEditable = isEditable,
            onDayClick = onDayClick,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = isEditable,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut(tween(100)) + slideOutVertically(targetOffsetY = { it / 2 })
            ) {
                Text(
                    text = stringResource(R.string.edit_history_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.End
                )
            }
            if (isEditButtonVisible)
                IconButton(onClick = onEdit) {
                    Crossfade(
                        targetState = isEditable,
                        label = "edit_history_crossfade"
                    ) { isChecked ->
                        Icon(
                            imageVector = if (isChecked) Icons.Outlined.Check else Icons.Default.Edit,
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
}

@Composable
fun HabitHistoryGrid(
    entries: Flow<PagingData<EditableHistoryUi>>,
    goal: Int = 1,
    isEditable: Boolean,
    onDayClick: (Int) -> Unit = {},
    squareDp: Dp = 32.dp,
    squareOffsetDp: Dp = 1.dp,
    initialOffsetDp: Dp = 4.dp,
) {
    val items = entries.collectAsLazyPagingItems()

    val fadingBrushHeader =
        remember { Brush.horizontalGradient(0f to Color.Transparent, 0.1f to Color.Red) }

    val minHeight = 8 * squareDp

    Column(
        modifier = Modifier
            .padding(initialOffsetDp)
            .fillMaxWidth()
            .wrapContentHeight()
            .defaultMinSize(minHeight = minHeight)
    ) {
        LazyRow(
            modifier = Modifier
                .padding(start = initialOffsetDp)
                .leftFadingEdge(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    width = 4.dp
                ),
            contentPadding = PaddingValues(2.dp),
            reverseLayout = true,
        ) {
            stickyHeader {
                Column(
                    Modifier
                        .size(width = squareDp, height = squareDp.times(8))
                        .fadingEdge(fadingBrushHeader)
                        .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Box(modifier = Modifier.size(squareDp))
                    for (index in 0 until 7)
                        WeekDayLabel(squareDp, index)
                }
            }
            items(
                count = items.itemCount,
                key = items.itemKey { it.month.timestamp },
                contentType = { items.itemContentType { it.month.weeks } }) { index ->
                items[index]?.let { item ->
                    MonthWithEntries(item, squareDp, squareOffsetDp, goal, isEditable, onDayClick)
                }
            }
        }
    }
}

@Composable
private fun MonthWithEntries(
    item: EditableHistoryUi,
    squareDp: Dp,
    squareOffsetDp: Dp,
    goal: Int,
    isEditable: Boolean,
    onDayClick: (Int) -> Unit
) {
    Column {
        MonthLabel(
            month = item.month,
            entrySize = squareDp,
            padding = squareOffsetDp
        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(7),
            modifier = Modifier
                .width(squareDp.times(item.month.weeks))
                .height(squareDp.times(7)),
            reverseLayout = true,
            userScrollEnabled = false
        ) {
            items(
                items = item.entries,
                key = { it.daysAgo },
                contentType = { "entry" }) { entry ->
                DailyEntry(
                    entry,
                    goal,
                    isEditable,
                    onDayClick,
                    squareDp,
                    squareOffsetDp,
                )
            }
        }
    }
}

@Composable
private fun WeekDayLabel(
    squareDp: Dp,
    index: Int
) {
    Box(
        modifier = Modifier.size(squareDp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = getWeekDisplayName(getLocale(), index),
            modifier = Modifier.padding(start = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DailyEntry(
    entry: EntryEditableUi,
    goal: Int = 1,
    isEditable: Boolean,
    onDayClick: (Int) -> Unit,
    size: Dp,
    padding: Dp,
) {
    PlainTooltipBox(
        tooltip = {
            Text(stringResource(R.string.entry_tooltip_percent_done, entry.timesDone, goal))
        }
    ) {
        Box(
            modifier = Modifier
                .tooltipAnchor()
                .size(size)
                .padding(padding)
                .background(entry.color, shape = MaterialTheme.shapes.extraSmall)
                .clickable(enabled = isEditable && entry.daysAgo >= 0) {
                    onDayClick(entry.daysAgo)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = entry.day,
                color = entry.color.contrastColor(),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun MonthLabel(
    month: MonthUi,
    entrySize: Dp,
    padding: Dp
) {
    Row(
        modifier = Modifier
            .width(entrySize.times(month.weeks))
            .height(entrySize)
            .padding(padding),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = month.getDisplayName(getLocale()),
            style = MaterialTheme.typography.labelSmall,
            overflow = TextOverflow.Ellipsis,
            textAlign = month.alignment,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.width(2.dp))
        if (month.weeks > 1) Text(
            text = month.getDisplayYear(getLocale()),
            style = MaterialTheme.typography.labelSmall,
            overflow = TextOverflow.Ellipsis,
            textAlign = month.alignment,
            maxLines = 1,
        )
    }
}

@Composable
@Preview
fun DarkCalendarEditableGridPreview(@PreviewParameter(HabitsEditableProvider::class) entries: List<EditableHistoryUi>) {
    HabitMateTheme(darkTheme = true) {
        Surface(modifier = Modifier.padding(16.dp)) {
            HabitHistory(flowOf(PagingData.from(entries)))
        }
    }
}