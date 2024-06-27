package com.github.skytoph.taski.presentation.settings.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun GeneralSettingsScreen() {
    GeneralContent()
}

@Composable
private fun GeneralContent(weekStartChange: (Boolean) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        SwitchSetting(weekStartChange, "week starts with", true)
        CheckboxSetting(weekStartChange, "highlight current day", true)
        CheckboxSetting(weekStartChange, "show streak borders", true)
    }
}

@Composable
fun CheckboxSetting(onChange: (Boolean) -> Unit, title: String, isChecked: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground
        )

        Checkbox(
            checked = isChecked,
            onCheckedChange = onChange,
        )
    }
}

@Composable
private fun SwitchSetting(onChange: (Boolean) -> Unit, title: String, isChecked: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground
        )

        Switch(
            checked = isChecked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                uncheckedBorderColor = Color.Transparent,
                uncheckedTrackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                uncheckedThumbColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

@Composable
@Preview
private fun GeneralSettingsScreenPreview() {
    HabitMateTheme(darkTheme = true) {
        GeneralContent()
    }
}

