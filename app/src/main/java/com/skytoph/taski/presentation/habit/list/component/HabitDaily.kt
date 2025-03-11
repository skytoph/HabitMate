@file:OptIn(ExperimentalMaterial3Api::class)

package com.skytoph.taski.presentation.habit.list.component

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.component.HabitTitleWithIcon
import com.skytoph.taski.presentation.core.component.ProgressCircle
import com.skytoph.taski.presentation.core.preview.HabitProvider
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.skytoph.taski.presentation.habit.list.EntryUi
import com.skytoph.taski.presentation.habit.list.HistoryUi
import com.skytoph.taski.ui.theme.HabitMateTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitDaily(
    habit: HabitUi,
    history: HistoryUi,
    updateEntries: (Int) -> Unit = {},
    onDone: (HabitUi, Int) -> Unit = { _, _ -> },
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val entriesNumber = remember { mutableIntStateOf(0) }
    LaunchedEffect(entriesNumber.value) {
        if (entriesNumber.value != 0) updateEntries(entriesNumber.value)
    }
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        entriesNumber.value = calculateNumberOfDailyEntries(maxWidth = maxWidth)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(CardDefaults.shape)
                .combinedClickable(onClick = onClick, onLongClick = onLongClick)
                .semantics { contentDescription = "habit" },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                HabitTitleWithIcon(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    icon = habit.icon.vector(LocalContext.current),
                    color = habit.color,
                    title = habit.title
                )
                AnimatedVisibility(
                    visible = entriesNumber.value == history.entries.size,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.entries_daily_spaced_by))) {
                        items(items = history.entries, key = { it.daysAgo }) { entry ->
                            CheckIcon(
                                onDone = onDone,
                                habit = habit,
                                percentDone = entry.percentDone,
                                daysAgo = entry.daysAgo,
                                timesDone = entry.timesDone
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckIcon(
    onDone: (HabitUi, Int) -> Unit,
    habit: HabitUi,
    percentDone: Float,
    daysAgo: Int,
    timesDone: Int
) {
    val defaultColor = MaterialTheme.colorScheme.tertiary
    val color =
        remember { Animatable(if (percentDone >= 1f) habit.color else defaultColor) }
    LaunchedEffect(timesDone) {
        color.animateTo(
            targetValue = if (percentDone >= 1f) habit.color else defaultColor,
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = if (habit.goal > 1 && percentDone >= 1f) 300 else 0
            )
        )
    }
    IconButton(onClick = { onDone(habit, daysAgo) }) {
        Box {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.check),
                contentDescription = null,
                modifier = Modifier
                    .background(color = color.value, shape = CircleShape)
                    .size(26.dp)
                    .padding(6.dp),
                tint = Color.White
            )
            if (habit.goal > 1) ProgressCircle(
                goal = habit.goal,
                done = timesDone,
                size = 26.dp,
                color = habit.color
            )
        }
    }
}

@Composable
@Preview
fun DarkHabitDailyPreview(
    @PreviewParameter(HabitProvider::class, limit = 1) habit: HabitWithHistoryUi<HistoryUi>
) {
    HabitMateTheme(darkTheme = true) {
        HabitDaily(habit = habit.habit, history = HistoryUi(entries = MutableList(5) { EntryUi(daysAgo = it) }))
    }
}

@Composable
@Preview
fun HabitDailyPreview(
    @PreviewParameter(HabitProvider::class, limit = 1) habit: HabitWithHistoryUi<HistoryUi>
) {
    HabitMateTheme(darkTheme = false) {
        HabitDaily(habit = habit.habit, history = HistoryUi(entries = MutableList(5) { EntryUi(daysAgo = it) }))
    }
}