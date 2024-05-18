package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.HabitTitleWithIcon
import com.github.skytoph.taski.presentation.core.preview.HabitProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.applyColor
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme


@Composable
fun HabitDaily(
    modifier: Modifier = Modifier,
    habit: HabitUi,
    history: HistoryUi,
    updateEntries: (Int) -> Unit = {},
    onDone: (HabitUi, Int) -> Unit = { _, _ -> },
) {
    val defaultColor = MaterialTheme.colorScheme.secondaryContainer
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val entries = calculateNumberOfDailyEntries(maxWidth = maxWidth)
        updateEntries(entries)
        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
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
                    modifier = Modifier.weight(1f),
                    icon = habit.icon.vector(LocalContext.current),
                    color = habit.color,
                    title = habit.title
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.entries_daily_spaced_by))) {
                    if (entries == history.entries.size)
                        items(history.entries) { entry ->
                            IconButton(onClick = { onDone(habit, entry.daysAgo) }) {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(
                                            color = habit.color.applyColor(
                                                defaultColor,
                                                entry.percentDone
                                            ),
                                            shape = MaterialTheme.shapes.large
                                        )
                                        .padding(4.dp),
                                    tint = Color.White
                                )
                            }
                        }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DarkHabitDailyPreview(
    @PreviewParameter(HabitProvider::class, limit = 1) habit: HabitWithHistoryUi<HistoryUi>
) {
    HabitMateTheme(darkTheme = true) {
        HabitDaily(habit = habit.habit, history = habit.history)
    }
}