@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)

package com.github.skytoph.taski.presentation.habit.details.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
import com.github.skytoph.taski.presentation.core.component.WeekDayLabel
import com.github.skytoph.taski.presentation.core.component.getLocale
import com.github.skytoph.taski.presentation.core.fadingEdge
import com.github.skytoph.taski.presentation.core.leftFadingEdge
import com.github.skytoph.taski.presentation.core.preview.HabitsEditableProvider
import com.github.skytoph.taski.presentation.habit.applyColor
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
    habitColor: Color = IconsColors.Default,
    onEdit: () -> Unit = {},
) {
    Column(
        Modifier
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.extraSmall
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .animateContentSize()
    ) {
        HabitHistoryGrid(
            entries = entries,
            habitColor = habitColor,
            goal = goal,
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .clickable { onEdit() }
            .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.button_edit),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun HabitHistoryGrid(
    entries: Flow<PagingData<EditableHistoryUi>>,
    habitColor: Color = IconsColors.Default,
    goal: Int = 1,
    isEditable: Boolean = false,
    onDayClick: (Int) -> Unit = {},
    squareDp: Dp = 32.dp,
    squareOffsetDp: Dp = 1.dp,
    initialOffsetDp: Dp = 4.dp,
) {
    val items = entries.collectAsLazyPagingItems()

    val fadingBrushHeader =
        remember { Brush.horizontalGradient(0f to Color.Transparent, 0.05f to Color.Red) }

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
                    width = 2.dp
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
                    for (index in 1..7)
                        WeekDayLabel(
                            modifier = Modifier
                                .size(squareDp)
                                .padding(start = 4.dp),
                            index = index,
                            alignment = Alignment.CenterStart
                        )
                }
            }
            items(
                count = items.itemCount,
                key = items.itemKey { it.month.timestamp },
                contentType = { items.itemContentType { it.month.weeks } }) { index ->
                items[index]?.let { item ->
                    MonthWithEntries(
                        item,
                        habitColor,
                        squareDp,
                        squareOffsetDp,
                        goal,
                        isEditable,
                        onDayClick
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthWithEntries(
    item: EditableHistoryUi,
    habitColor: Color,
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
                items = item.entriesList,
                key = { it.daysAgo },
                contentType = { "entry" }) { entry ->
                DailyEntry(
                    entry,
                    habitColor,
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
private fun DailyEntry(
    entry: EntryEditableUi,
    habitColor: Color,
    goal: Int = 1,
    isEditable: Boolean,
    onDayClick: (Int) -> Unit,
    size: Dp,
    padding: Dp,
) {
    val color =
        habitColor.applyColor(MaterialTheme.colorScheme.onSecondaryContainer, entry.percentDone)
    TooltipBox(
        state = rememberTooltipState(),
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            Text(stringResource(R.string.entry_tooltip_percent_done, entry.timesDone, goal))
        }
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .padding(padding)
                .background(color, shape = MaterialTheme.shapes.extraSmall)
                .border(
                    width = 2.dp,
                    color = if (entry.hasBorder) habitColor else Color.Transparent,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .clickable(enabled = isEditable && entry.daysAgo >= 0) {
                    onDayClick(entry.daysAgo)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = entry.day,
                color = color.contrastColor(),
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
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
            textAlign = month.alignment,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.width(2.dp))
        if (month.weeks > 1) Text(
            text = month.getDisplayYear(getLocale()),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
            textAlign = month.alignment,
            maxLines = 1
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