@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)

package com.github.skytoph.taski.presentation.habit.edit.component

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
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
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
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.getLocale
import com.github.skytoph.taski.presentation.core.fadingEdge
import com.github.skytoph.taski.presentation.core.habitColor
import com.github.skytoph.taski.presentation.core.preview.HabitsEditableProvider
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.edit.MonthUi
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.ui.theme.TaskiTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun HabitHistory(
    entries: Flow<PagingData<EditableHistoryUi>>,
    goal: Int = 1,
    isEditable: Boolean = false,
    habitColor: Color = IconsColors.allColors.first(),
    onEdit: () -> Unit = {},
    onDayClick: (Int) -> Unit = {},
) {
    Column(
        Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(10f)
            )
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        HabitHistoryGrid(
            entries = entries,
            goal = goal,
            isEditable = isEditable,
            habitColor = habitColor,
            onDayClick = onDayClick,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isEditable)
                Text(
                    text = stringResource(R.string.edit_history_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.End
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
    entries: Flow<PagingData<EditableHistoryUi>>,
    goal: Int = 1,
    isEditable: Boolean,
    habitColor: Color = IconsColors.allColors.first(),
    onDayClick: (Int) -> Unit = {},
    squareDp: Dp = 32.dp,
    squareOffsetDp: Dp = 1.dp,
    initialOffsetDp: Dp = 4.dp,
) {
    val items = entries.collectAsLazyPagingItems()

    val fadingBrush = Brush.horizontalGradient(
        0f to Color.Transparent,
        0.1f to Color.Black,
        endX = with(LocalDensity.current) { (40.dp).toPx() }
    )

    val minHeight = 8 * squareDp

    Column(
        modifier = Modifier
            .padding(initialOffsetDp)
            .fillMaxWidth()
            .wrapContentHeight()
            .defaultMinSize(minHeight = minHeight)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10f)
            )
    ) {
        LazyRow(
            modifier = Modifier
                .padding(start = initialOffsetDp)
                .fadingEdge(fadingBrush),
            contentPadding = PaddingValues(2.dp),
            reverseLayout = true,
        ) {
            items(count = items.itemCount) { index ->
                items[index]?.let { item ->
                    LazyHorizontalStaggeredGrid(
                        rows = StaggeredGridCells.Fixed(8),
                        modifier = Modifier
                            .width(squareDp.times(item.month.weeks))
                            .height(minHeight),
                        reverseLayout = true,
                        userScrollEnabled = false
                    ) {
                        item {
                            MonthLabel(
                                month = item.month,
                                entrySize = squareDp,
                                padding = squareOffsetDp
                            )
                        }
                        items(items = item.entries, key = { it.daysAgo }) { entry ->
                            DailyEntry(
                                entry,
                                goal,
                                isEditable,
                                onDayClick,
                                squareDp,
                                squareOffsetDp,
                                habitColor
                            )
                        }
                    }
                }
            }
        }
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
    entryColor: Color
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
                .background(
                    habitColor(
                        entry.colorPercent(goal),
                        MaterialTheme.colorScheme.onSecondaryContainer,
                        entryColor
                    ),
                    shape = RoundedCornerShape(10)
                )
                .clickable(enabled = isEditable && entry.daysAgo >= 0) {
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
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.labelMedium,
            overflow = TextOverflow.Ellipsis,
            textAlign = month.alignment,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = month.getDisplayYear(getLocale()),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
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
    TaskiTheme(darkTheme = true) {
        Surface(modifier = Modifier.padding(16.dp)) {
            HabitHistory(flowOf(PagingData.from(entries)))
        }
    }
}