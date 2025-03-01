package com.github.skytoph.taski.presentation.habit.details.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.LoadingItems
import com.github.skytoph.taski.presentation.core.component.ProgressCircle
import com.github.skytoph.taski.presentation.core.component.WeekDayLabel
import com.github.skytoph.taski.presentation.core.component.getLocale
import com.github.skytoph.taski.presentation.core.component.weekDayCalendar
import com.github.skytoph.taski.presentation.core.preview.HabitsEditableProvider
import com.github.skytoph.taski.presentation.habit.applyColor
import com.github.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.github.skytoph.taski.presentation.habit.edit.EntryEditableUi
import com.github.skytoph.taski.presentation.habit.edit.StreakType
import com.github.skytoph.taski.presentation.habit.icon.IconsColors
import com.github.skytoph.taski.ui.theme.HabitMateTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun MonthlyPager(
    items: LazyPagingItems<EditableHistoryUi>,
    pagerState: PagerState,
    goal: Int = 1,
    habitColor: Color = IconsColors.Default,
    isFirstDaySunday: Boolean = false,
    isEditable: Boolean = false,
    onEdit: (Int) -> Unit = {},
    squareDp: Dp = 40.dp,
) {
    val coroutineScope = rememberCoroutineScope()

    if (items.loadState.refresh == LoadState.Loading || items.itemCount == 0) Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = squareDp.times(7)),
        contentAlignment = Alignment.Center
    ) {
        LoadingItems()
    }
    else Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.extraSmall
            )
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .animateContentSize()
            .widthIn(max = squareDp.times(8)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(enabled = pagerState.canScrollForward) {
                        coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                    contentDescription = "previous month",
                    tint = if (pagerState.canScrollForward) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                )
            }
            Spacer(Modifier.width(8.dp))
            Crossfade(
                targetState = pagerState.currentPage,
                label = "habit_history_month_crossfade",
                animationSpec = tween(durationMillis = 300, easing = LinearEasing)
            ) { page ->
                if (page < items.itemCount) items[page]?.let {
                    Text(
                        text = it.month.getDisplayName(getLocale()),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(enabled = pagerState.canScrollBackward) {
                        coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.chevron_right),
                    contentDescription = "next month",
                    tint = if (pagerState.canScrollBackward) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            reverseLayout = true,
            beyondViewportPageCount = 1,
            key = { it },
            verticalAlignment = Alignment.Top,
        ) { page ->
            items[page]?.let { item ->
                MonthlyView(
                    entries = item.entriesList,
                    goal = goal,
                    habitColor = habitColor,
                    isFirstDaySunday = isFirstDaySunday,
                    isEditable = isEditable,
                    onEdit = onEdit,
                    squareDp = squareDp
                )
            }
        }
    }
}

@Composable
fun MonthlyView(
    entries: List<EntryEditableUi>,
    goal: Int = 1,
    habitColor: Color = IconsColors.Default,
    isFirstDaySunday: Boolean = false,
    onEdit: (Int) -> Unit = {},
    isEditable: Boolean = false,
    squareDp: Dp = 40.dp,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        userScrollEnabled = false,
        modifier = Modifier
            .wrapContentHeight()
            .heightIn(min = 0.dp, max = squareDp.times(8)),
        horizontalArrangement = Arrangement.Center
    ) {
        items(items = (1..7).toList()) { index ->
            WeekDayLabel(
                modifier = Modifier.padding(vertical = 4.dp),
                index = weekDayCalendar(isFirstDaySunday, index),
                alignment = Alignment.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        }
        items(items = entries, key = { it.daysAgo }) { entry ->
            EntryItem(
                day = entry.day,
                daysAgo = entry.daysAgo,
                timesDone = entry.timesDone,
                percentDone = entry.timesDone / goal,
                goal = goal,
                habitColor = habitColor,
                streakType = entry.streakType,
                squareDp = squareDp,
                isEditable = isEditable,
                isDisabled = entry.isDisabled,
                onClick = { onEdit(entry.daysAgo) }
            )
        }
    }
}

@Composable
private fun EntryItem(
    day: String,
    daysAgo: Int,
    timesDone: Int,
    percentDone: Int,
    goal: Int,
    habitColor: Color,
    streakType: StreakType?,
    onClick: () -> Unit,
    isEditable: Boolean = false,
    isDisabled: Boolean = false,
    squareDp: Dp = 50.dp,
    streakColor: Color = habitColor.applyColor(MaterialTheme.colorScheme.tertiaryContainer, 0.2f)
) {
    val defaultColor = MaterialTheme.colorScheme.tertiaryContainer
    val color = remember { Animatable(if (percentDone >= 1f) habitColor else defaultColor) }
    val disabledColor = habitColor.applyColor(MaterialTheme.colorScheme.tertiaryContainer, 0.5f)
    LaunchedEffect(timesDone) {
        color.animateTo(
            targetValue = if (percentDone >= 1f) habitColor else defaultColor,
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = if (goal > 1 && percentDone >= 1f) 300 else 0
            )
        )
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.aspectRatio(1f)
    ) {
        Box(
            modifier = Modifier
                .size(squareDp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .clickable(
                    enabled = isEditable && !isDisabled,
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                )
        )
        Crossfade(targetState = streakType, label = "streak_crossfade_$daysAgo") { type ->
            type?.let {
                StreakLine(type = type, color = streakColor, squareSize = 48.dp)
            }
        }
        Text(
            text = day,
            color = if (isDisabled) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f) else MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        ProgressCircle(
            goal = goal,
            done = timesDone,
            size = 30.dp,
            color = if (isDisabled) disabledColor else habitColor,
            isHintShown = false
        )
    }
}

@Preview
@Composable
private fun MonthlyPreview(@PreviewParameter(HabitsEditableProvider::class) entries: List<EditableHistoryUi>) {
    HabitMateTheme(darkTheme = true) {
        MonthlyPager(
            goal = 4,
            isFirstDaySunday = false,
            items = flowOf(PagingData.from(entries)).collectAsLazyPagingItems(),
            pagerState = rememberPagerState(pageCount = { 1 })
        )
    }
}