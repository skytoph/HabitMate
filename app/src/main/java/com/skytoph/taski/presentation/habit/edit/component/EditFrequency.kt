package com.skytoph.taski.presentation.habit.edit.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skytoph.taski.presentation.core.component.getLocale
import com.skytoph.taski.presentation.habit.edit.frequency.FrequencyCustomType
import com.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
import com.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun EditFrequency(
    frequency: FrequencyState = FrequencyState(),
    expanded: Boolean = true,
    expand: () -> Unit = {},
    minHeight: Dp = 48.dp,
    selectType: (FrequencyUi) -> Unit = {},
    selectDay: (Int) -> Unit = {},
    increaseTimes: () -> Unit = {},
    decreaseTimes: () -> Unit = {},
    increaseType: () -> Unit = {},
    decreaseType: () -> Unit = {},
    selectCustomType: (FrequencyCustomType) -> Unit = {},
    expandType: () -> Unit = {},
    typeExpanded: Boolean = true,
    isFirstDaySunday: Boolean = false,
) {
    Column(
        modifier = Modifier
            .also { if (!expanded) it.height(minHeight) }
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .fillMaxWidth()
                .defaultMinSize(minHeight = minHeight)
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable { expand() }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Crossfade(targetState = frequency, label = "frequency_summary_crossfade", modifier = Modifier.weight(1f)) {
                Text(
                    text = it.selected.summarize(LocalContext.current.resources, isFirstDaySunday, getLocale()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "select reminder",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
        }
        if (expanded)
            FrequencySettings(
                frequency = frequency,
                selectType = selectType,
                selectDay = selectDay,
                increaseTimes = increaseTimes,
                decreaseTimes = decreaseTimes,
                increaseType = increaseType,
                decreaseType = decreaseType,
                selectCustomType = selectCustomType,
                expandType = expandType,
                typeExpanded = typeExpanded,
                isFirstDaySunday = isFirstDaySunday
            )
    }
}

@Composable
@Preview
private fun FrequencyPreview() {
    HabitMateTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            EditFrequency()
        }
    }
}