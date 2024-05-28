package com.github.skytoph.taski.presentation.habit.edit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.presentation.core.component.WeekDayLabel
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun FrequencyDaily(
    frequency: FrequencyState.Daily = FrequencyState.Daily(),
    select: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            for (index in 1..7)
                WeekDayLabel(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (frequency.days.contains(index)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.shapes.extraSmall
                        )
                        .clickable { select(index) }
                        .padding(vertical = 6.dp),
                    index = index,
                    alignment = Alignment.Center,
                    color = MaterialTheme.colorScheme.onTertiary
                )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    HabitMateTheme(darkTheme = true) {
        FrequencySettings(frequency = FrequencyState.Daily())
    }
}