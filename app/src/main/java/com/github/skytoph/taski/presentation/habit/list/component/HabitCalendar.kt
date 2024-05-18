package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.github.skytoph.taski.presentation.core.color.habitColor
import com.github.skytoph.taski.presentation.core.component.HabitTitleWithIcon
import com.github.skytoph.taski.presentation.core.preview.HabitProvider
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.HabitWithHistoryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme


@Composable
fun HabitCalendar(
    modifier: Modifier = Modifier,
    onDone: () -> Unit,
    habit: HabitUi,
    history: HistoryUi,
    updateEntries: (Int) -> Unit = {},
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val entries = calculateNumberOfCalendarEntries(maxWidth = maxWidth)
        updateEntries(entries)
        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HabitTitleWithIcon(
                        modifier = Modifier.weight(1f),
                        icon = habit.icon.vector(LocalContext.current),
                        color = habit.color,
                        title = habit.title
                    )
                    IconButton(onClick = onDone) {
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
                                .padding(4.dp),
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
        HabitCalendar(
            onDone = {},
            habit = habit.habit,
            history = habit.history,
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DarkHabitCardPreview(
    @PreviewParameter(HabitProvider::class, limit = 1) habit: HabitWithHistoryUi<HistoryUi>
) {
    HabitMateTheme(darkTheme = true) {
        HabitCalendar(
            onDone = {},
            habit = habit.habit,
            history = habit.history,
        )
    }
}