@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)

package com.github.skytoph.taski.presentation.habit.details.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import com.github.skytoph.taski.presentation.core.component.ButtonWithBackground
import com.github.skytoph.taski.presentation.core.component.WeekDayLabel
import com.github.skytoph.taski.presentation.core.component.getLocale
import com.github.skytoph.taski.presentation.core.component.weekDayCalendar
import com.github.skytoph.taski.presentation.core.leftFadingEdge
import com.github.skytoph.taski.presentation.core.preview.HabitsEditableProvider
import com.github.skytoph.taski.presentation.habit.applyColor
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.edit.MonthUi
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.presentation.habit.list.mapper.ColorPercentMapper
import com.github.skytoph.taski.ui.theme.HabitMateTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar

@Composable
fun HabitHistory(
    entries: Flow<PagingData<EditableHistoryUi>>,
    goal: Int = 1,
    habitColor: Color = IconsColors.Default,
    isFirstDaySunday: Boolean = false,
    onEdit: () -> Unit = {},
    onChangeView: () -> Unit = {},
    isCalendarView: Boolean = true
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.extraSmall
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            if (isCalendarView)
                MonthlyPager(
                    entries = entries,
                    habitColor = habitColor,
                    goal = goal,
                    isFirstDaySunday = isFirstDaySunday,
                )
            else
                HabitHistoryGrid(
                    entries = entries,
                    habitColor = habitColor,
                    goal = goal,
                    isFirstDaySunday = isFirstDaySunday,
                )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.onTertiaryContainer, MaterialTheme.shapes.small
                    )
                    .clip(MaterialTheme.shapes.small)
                    .clickable(onClick = onChangeView)
                    .size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(if (isCalendarView) R.drawable.table else R.drawable.calendar_days),
                    contentDescription = "calendar view",
                    tint = Color.White
                )
            }
            ButtonWithBackground(
                onClick = onEdit,
                text = stringResource(R.string.button_edit),
                background = MaterialTheme.colorScheme.onTertiaryContainer,
                height = 32.dp,
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
    isFirstDaySunday: Boolean,
) {
    val items = entries.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .padding(initialOffsetDp)
            .fillMaxWidth()
            .wrapContentHeight()
            .defaultMinSize(minHeight = 8 * squareDp)
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
                        .background(color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.9f))
                ) {
                    Box(modifier = Modifier.size(squareDp))
                    for (index in 1..7)
                        WeekDayLabel(
                            modifier = Modifier
                                .size(squareDp)
                                .padding(start = 4.dp),
                            index = weekDayCalendar(isFirstDaySunday, index),
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
    val defaultColor = Color.White
    val color = remember { Animatable(entryColor(entry, habitColor, defaultColor, isEditable, goal)) }
    LaunchedEffect(entry.timesDone, entry.hasBorder) {
        color.animateTo(
            targetValue = entryColor(entry, habitColor, defaultColor, isEditable, goal),
            animationSpec = tween(durationMillis = 300)
        )
    }
    TooltipBox(
        state = rememberTooltipState(),
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.entry_tooltip_percent_done, entry.timesDone, goal),
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .padding(padding)
                .background(
                    color = color.value,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .clip(shape = MaterialTheme.shapes.extraSmall)
                .border(
                    width = 2.dp,
                    color = if (entry.hasBorder) habitColor.applyColor(defaultColor, 0.1f) else Color.Transparent,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .clickable(enabled = isEditable && entry.daysAgo >= 0) {
                    onDayClick(entry.daysAgo)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = entry.day,
                color = color.value.contrastColor(),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

private fun entryColor(
    entry: EntryEditableUi, habitColor: Color, defaultColor: Color, isEditable: Boolean, goal: Int
) = when {
    entry.daysAgo < 0 -> Color.Gray.applyColor(defaultColor, 0.3f)
    entry.hasBorder -> habitColor.applyColor(defaultColor, 0.5f)
    else -> habitColor.applyColor(defaultColor, ColorPercentMapper.toColorPercent(entry.timesDone, goal))
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
            text = month.getDisplayMonth(getLocale()),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
            textAlign = month.alignment,
            maxLines = 1,
        )
        if ((month.index == Calendar.JANUARY || month.index == Calendar.JULY || month.index == Calendar.DECEMBER) && month.weeks > 1) {
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = month.getDisplayYear(getLocale()),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                textAlign = month.alignment,
                maxLines = 1
            )
        }
    }
}

@Composable
@Preview
fun DarkEditableGridPreview(@PreviewParameter(HabitsEditableProvider::class) entries: List<EditableHistoryUi>) {
    HabitMateTheme(darkTheme = true) {
        Surface {
            HabitHistory(flowOf(PagingData.from(entries)), isFirstDaySunday = false)
        }
    }
}

@Composable
@Preview
fun DarkCalendarEditableGridPreview(@PreviewParameter(HabitsEditableProvider::class) entries: List<EditableHistoryUi>) {
    HabitMateTheme(darkTheme = true) {
        Surface {
            HabitHistory(flowOf(PagingData.from(entries)), isCalendarView = false)
        }
    }
}