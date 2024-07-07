@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.color.habitColor
import com.github.skytoph.taski.presentation.core.component.HabitTitleWithIcon
import com.github.skytoph.taski.presentation.core.preview.HabitProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme


@Composable
fun HabitCalendar(
    onDone: () -> Unit = {},
    habit: HabitUi,
    history: HistoryUi,
    updateEntries: (Int) -> Unit = {},
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val entries = calculateNumberOfCalendarEntries(maxWidth = maxWidth)
        updateEntries(entries)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(CardDefaults.shape)
                .combinedClickable(onClick = onClick, onLongClick = onLongClick)
                .semantics { contentDescription = "habit" },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 4.dp)
                ) {
                    HabitTitleWithIcon(
                        modifier = Modifier.weight(1f),
                        icon = habit.icon.vector(LocalContext.current),
                        color = habit.color,
                        title = habit.title
                    )
                    Box(Modifier.clickable { onDone() }) {
                        val defaultColor = MaterialTheme.colorScheme.secondaryContainer
                        val color = habitColor(history.todayDonePercent, defaultColor, habit.color)
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color = color,
                                    shape = RoundedCornerShape(30)
                                )
                                .padding(6.dp),
                            tint = Color.White
                        )
                    }
                }
                val padding = dimensionResource(R.dimen.entry_calendar_content_padding)
                HabitHistoryTable(
                    Modifier.padding(start = padding, end = padding, bottom = padding),
                    habit.color,
                    history.entries
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitCardPreview(
    @PreviewParameter(HabitProvider::class, limit = 1) habit: HabitWithHistoryUi<HistoryUi>
) {
    HabitMateTheme {
        HabitCalendar(habit = habit.habit, history = habit.history)
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DarkHabitCardPreview(
    @PreviewParameter(HabitProvider::class, limit = 1) habit: HabitWithHistoryUi<HistoryUi>
) {
    HabitMateTheme(darkTheme = true) {
        HabitCalendar(habit = habit.habit, history = habit.history)
    }
}