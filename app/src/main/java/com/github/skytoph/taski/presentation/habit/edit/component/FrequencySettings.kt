package com.github.skytoph.taski.presentation.habit.edit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyCustomType
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun FrequencySettings(
    frequency: FrequencyState = FrequencyState(),
    selectType: (FrequencyUi) -> Unit = {},
    selectDay: (Int) -> Unit = {},
    increaseTimes: () -> Unit = {},
    decreaseTimes: () -> Unit = {},
    increaseType: () -> Unit = {},
    decreaseType: () -> Unit = {},
    selectCustomType: (FrequencyCustomType) -> Unit = {},
    expandType: () -> Unit = {},
    typeExpanded: Boolean = false
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
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
                selected = frequency.isSelected(frequency.daily),
                select = { selectType(frequency.daily) })
            VerticalDivider(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp),
                color = if (frequency.isSelected(frequency.custom)) DividerDefaults.color else Color.Transparent
            )
            FrequencyOption(
                modifier = Modifier.weight(1f),
                title = "monthly",
                selected = frequency.isSelected(frequency.monthly),
                select = { selectType(frequency.monthly) })
            VerticalDivider(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp),
                color = if (frequency.isSelected(frequency.daily)) DividerDefaults.color else Color.Transparent
            )
            FrequencyOption(
                modifier = Modifier.weight(1f),
                title = "custom",
                selected = frequency.isSelected(frequency.custom),
                select = { selectType(frequency.custom) })
        }
        FrequencySettingsContent(
            frequency = frequency.selected,
            selectDay = selectDay,
            increaseTimes = increaseTimes,
            decreaseTimes = decreaseTimes,
            increaseType = increaseType,
            decreaseType = decreaseType,
            selectType = selectCustomType,
            expandType = expandType,
            typeExpanded = typeExpanded
        )
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
    frequency: FrequencyUi,
    typeExpanded: Boolean,
    selectDay: (Int) -> Unit,
    increaseTimes: () -> Unit,
    decreaseTimes: () -> Unit,
    increaseType: () -> Unit,
    decreaseType: () -> Unit,
    selectType: (FrequencyCustomType) -> Unit,
    expandType: () -> Unit
) {
    when (frequency) {
        is FrequencyUi.Daily -> FrequencyDaily(frequency = frequency, select = selectDay)
        is FrequencyUi.Monthly -> FrequencyMonthly(frequency = frequency, select = selectDay)
        is FrequencyUi.Custom -> FrequencyCustom(
            frequency = frequency,
            increaseTimes = increaseTimes,
            decreaseTimes = decreaseTimes,
            increaseType = increaseType,
            decreaseType = decreaseType,
            selectType = selectType,
            expandType = expandType,
            typeExpanded = typeExpanded
        )

        is FrequencyUi.Everyday -> FrequencySettingsContent(
            frequency.frequency,
            typeExpanded,
            selectDay,
            increaseTimes,
            decreaseTimes,
            increaseType,
            decreaseType,
            selectType,
            expandType
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