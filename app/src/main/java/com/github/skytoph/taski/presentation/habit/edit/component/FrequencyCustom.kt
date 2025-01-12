package com.github.skytoph.taski.presentation.habit.edit.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.MenuOptionComponent
import com.github.skytoph.taski.presentation.core.component.SquareButton
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyCustomType
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
    selectType: (FrequencyCustomType) -> Unit = {},
    expandType: () -> Unit = {},
    typeExpanded: Boolean = true,
    buttonSize: Dp = 40.dp,
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
                size = buttonSize,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            SquareButton(
                onClick = increaseTimes,
                icon = Icons.Default.Add,
                isEnabled = frequency.timesCount.canBeIncreased,
                size = buttonSize,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            FrequencyCounter(count = frequency.typeCount.value)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .defaultMinSize(minHeight = buttonSize)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = expandType
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(id = frequency.frequencyType.title),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (typeExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "select reminder",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            SquareButton(
                onClick = decreaseType,
                icon = Icons.Default.Remove,
                isEnabled = frequency.typeCount.canBeDecreased,
                size = buttonSize,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            SquareButton(
                onClick = increaseType,
                icon = Icons.Default.Add,
                isEnabled = frequency.typeCount.canBeIncreased,
                size = buttonSize,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
        }
        if (typeExpanded) Column(modifier = Modifier.clip(MaterialTheme.shapes.small)) {
            MenuOptionComponent(
                option = FrequencyCustomType.Day,
                selected = frequency.frequencyType == FrequencyCustomType.Day,
                select = selectType,
                title = stringResource(FrequencyCustomType.Day.title)
            )
            HorizontalDivider()
            MenuOptionComponent(
                option = FrequencyCustomType.Week,
                selected = frequency.frequencyType == FrequencyCustomType.Week,
                select = selectType,
                title = stringResource(FrequencyCustomType.Week.title)
            )
            HorizontalDivider()
            MenuOptionComponent(
                option = FrequencyCustomType.Month,
                selected = frequency.frequencyType == FrequencyCustomType.Month,
                select = selectType,
                title = stringResource(FrequencyCustomType.Month.title)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    HabitMateTheme(darkTheme = true) {
        FrequencySettings(frequency = FrequencyState(selectedName = FrequencyUi.Custom().name), typeExpanded = true)
    }
}