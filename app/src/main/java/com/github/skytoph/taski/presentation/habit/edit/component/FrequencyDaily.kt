package com.github.skytoph.taski.presentation.habit.edit.component

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.presentation.core.color.contrastColor
import com.github.skytoph.taski.presentation.core.component.WeekDayLabel
import com.github.skytoph.taski.presentation.core.component.weekDayCalendar
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun FrequencyDaily(
    frequency: FrequencyUi.Daily = FrequencyUi.Daily(),
    select: (Int) -> Unit,
    isFirstDaySunday: Boolean
) {
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val primary = MaterialTheme.colorScheme.primary

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            for (index in 1..7) {
                val item = weekDayCalendar(isFirstDaySunday, index)
                val color = remember { Animatable(if (frequency.days.contains(item)) primary else surfaceVariant) }
                LaunchedEffect(frequency.days) {
                    color.animateTo(
                        targetValue = if (frequency.days.contains(item)) primary else surfaceVariant,
                        animationSpec = tween(300)
                    )
                }
                WeekDayLabel(
                    modifier = Modifier
                        .weight(1f)
                        .background(color = color.value, shape = MaterialTheme.shapes.extraSmall)
                        .clip(shape = MaterialTheme.shapes.extraSmall)
                        .clickable { select(item) }
                        .padding(vertical = 6.dp),
                    index = item,
                    alignment = Alignment.Center,
                    color = color.value.contrastColor(),
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
private fun Preview() {
    HabitMateTheme(darkTheme = true) {
        FrequencySettings(isFirstDaySunday = true)
    }
}