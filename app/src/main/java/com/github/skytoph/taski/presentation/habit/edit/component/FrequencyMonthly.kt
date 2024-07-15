package com.github.skytoph.taski.presentation.habit.edit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.presentation.core.color.contrastColor
import com.github.skytoph.taski.presentation.core.component.WeekDayLabel
import com.github.skytoph.taski.presentation.core.component.weekDayCalendar
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun FrequencyMonthly(
    frequency: FrequencyUi.Monthly = FrequencyUi.Monthly(),
    select: (Int) -> Unit = {},
    squareDp: Dp = 40.dp,
    isFirstDaySunday: Boolean
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        userScrollEnabled = false,
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier
            .wrapContentHeight()
            .heightIn(min = 0.dp, max = squareDp.times(8))
    ) {
        items(items = (1..7).toList()) { index ->
            WeekDayLabel(
                modifier = Modifier.padding(vertical = 4.dp),
                index = weekDayCalendar(isFirstDaySunday, index),
                alignment = Alignment.Center,
            )
        }
        items(items = (1..31).toList(), key = { it }) { index ->
            MonthlyItem(
                squareDp = squareDp,
                index = index,
                selected = frequency.days.contains(index),
                select = select
            )
        }
    }
}

@Composable
private fun MonthlyItem(
    squareDp: Dp,
    index: Int,
    selected: Boolean = false,
    select: (Int) -> Unit
) {
    val background =
        if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(squareDp)
                .background(color = background, shape = CircleShape)
                .aspectRatio(1f)
                .clip(CircleShape)
                .clickable { select(index) }
        )
        Text(text = index.toString(), color = background.contrastColor(), style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview
@Composable
private fun MonthlyPreview() {
    HabitMateTheme(darkTheme = true) {
        FrequencySettings(frequency = FrequencyState(selectedName = FrequencyUi.Monthly().name))
    }
}