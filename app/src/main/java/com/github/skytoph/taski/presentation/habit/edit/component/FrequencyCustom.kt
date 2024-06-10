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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.SquareButton
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun FrequencyCustom(
    frequency: FrequencyUi.Custom,
    increaseTimes: () -> Unit = {},
    decreaseTimes: () -> Unit = {},
    increaseType: () -> Unit = {},
    decreaseType: () -> Unit = {},
    counterSize: Dp = 40.dp
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FrequencyCounter(count = frequency.timesCount.value)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = pluralStringResource(
                    id = R.plurals.times_label, count = frequency.timesCount.value
                ),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            SquareButton(
                onClick = decreaseTimes,
                icon = Icons.Default.Remove,
                isEnabled = frequency.timesCount.canBeDecreased,
                size = 40.dp,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            SquareButton(
                onClick = increaseTimes,
                icon = Icons.Default.Add,
                isEnabled = frequency.timesCount.canBeIncreased,
                size = 40.dp,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FrequencyCounter(count = frequency.typeCount.value)
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    text = pluralStringResource(
                        id = frequency.frequencyType.title, count = frequency.typeCount.value
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            SquareButton(
                onClick = decreaseType,
                icon = Icons.Default.Remove,
                isEnabled = frequency.typeCount.canBeDecreased,
                size = counterSize,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            SquareButton(
                onClick = increaseType,
                icon = Icons.Default.Add,
                isEnabled = frequency.typeCount.canBeIncreased,
                size = counterSize,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    HabitMateTheme(darkTheme = true) {
        FrequencySettings(frequency = FrequencyState(selectedName = FrequencyUi.Custom().name))
    }
}