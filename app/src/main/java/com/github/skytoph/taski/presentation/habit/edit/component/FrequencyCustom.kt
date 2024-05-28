package com.github.skytoph.taski.presentation.habit.edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.SquareButton
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencySettingType
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun FrequencyCustom(counterSize: Dp = 40.dp) {
    val state = remember { mutableStateOf(FrequencyState()) }
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FrequencyCounter(text = state.value.timesCount.toString())
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = pluralStringResource(
                    id = R.plurals.times_label, count = state.value.timesCount
                ),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            SquareButton(
                onClick = {
                    state.value = state.value.copy(timesCount = state.value.timesCount - 1)
                },
                icon = Icons.Default.Remove,
                isEnabled = false,
                size = 40.dp
            )
            Spacer(modifier = Modifier.width(4.dp))
            SquareButton(
                onClick = {
                    state.value = state.value.copy(timesCount = state.value.timesCount + 1)
                },
                icon = Icons.Default.Add,
                isEnabled = true,
                size = 40.dp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FrequencyCounter(text = state.value.inCount.toString())
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    text = pluralStringResource(
                        id = state.value.frequencyType.title, count = state.value.inCount
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            SquareButton(
                onClick = { state.value = state.value.copy(inCount = state.value.inCount - 1) },
                icon = Icons.Default.Remove,
                isEnabled = false,
                size = counterSize
            )
            Spacer(modifier = Modifier.width(4.dp))
            SquareButton(
                onClick = { state.value = state.value.copy(inCount = state.value.inCount + 1) },
                icon = Icons.Default.Add,
                isEnabled = true,
                size = counterSize
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    HabitMateTheme(darkTheme = true) {
        FrequencySettings(frequency = FrequencySettingType.Custom)
    }
}