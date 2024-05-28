package com.github.skytoph.taski.presentation.habit.edit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun FrequencySettings(
    frequency: FrequencyState = FrequencyState.Custom(),
    selectType: (FrequencyState) -> Unit = {},
    selectDay: (Int) -> Unit = {},
    increaseTimes: () -> Unit = {},
    decreaseTimes: () -> Unit = {},
    increaseType: () -> Unit = {},
    decreaseType: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.extraSmall
            )
            .clip(MaterialTheme.shapes.extraSmall)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FrequencyOption(
                modifier = Modifier.weight(1f),
                title = "daily",
                selected = frequency is FrequencyState.Daily,
                select = { selectType(FrequencyState.Daily()) })
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp),
                color = if (frequency is FrequencyState.Custom) DividerDefaults.color else Color.Transparent
            )
            FrequencyOption(
                modifier = Modifier.weight(1f),
                title = "monthly",
                selected = frequency is FrequencyState.Monthly,
                select = { selectType(FrequencyState.Monthly()) })
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp),
                color = if (frequency is FrequencyState.Daily) DividerDefaults.color else Color.Transparent
            )
            FrequencyOption(
                modifier = Modifier.weight(1f),
                title = "custom",
                selected = frequency is FrequencyState.Custom,
                select = { selectType(FrequencyState.Custom()) })
        }
        Spacer(modifier = Modifier.height(8.dp))
        FrequencySettingsContent(
            frequency = frequency,
            increaseTimes = increaseTimes,
            decreaseTimes = decreaseTimes,
            increaseType = increaseType,
            decreaseType = decreaseType,
            selectDay = selectDay,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun FrequencyOption(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    title: String,
    select: () -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = modifier
            .background(
                if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .clickable { select() }
            .padding(vertical = 4.dp),
    )
}

@Composable
private fun FrequencySettingsContent(
    frequency: FrequencyState,
    selectDay: (Int) -> Unit,
    increaseTimes: () -> Unit,
    decreaseTimes: () -> Unit,
    increaseType: () -> Unit,
    decreaseType: () -> Unit
) {
    when (frequency) {
        is FrequencyState.Daily -> FrequencyDaily(frequency = frequency, select = selectDay)
        is FrequencyState.Monthly -> FrequencyMonthly(frequency = frequency, select = selectDay)
        is FrequencyState.Custom -> FrequencyCustom(
            frequency = frequency,
            increaseTimes = increaseTimes,
            decreaseTimes = decreaseTimes,
            increaseType = increaseType,
            decreaseType = decreaseType
        )
    }
}

@Preview
@Composable
private fun Preview() {
    HabitMateTheme(darkTheme = true) {
        FrequencySettings()
    }
}