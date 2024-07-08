package com.github.skytoph.taski.presentation.settings.general.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.datastore.Settings
import com.github.skytoph.taski.presentation.core.component.MenuItemText
import com.github.skytoph.taski.presentation.settings.general.GeneralSettingsEvent
import com.github.skytoph.taski.presentation.settings.general.GeneralSettingsViewModel
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun GeneralSettingsScreen(
    viewModel: GeneralSettingsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.initAppBar(title = R.string.settings_general_title)
    }

    GeneralContent(
        state = viewModel.state().collectAsState(),
        weekStartChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleWeekStart) },
        currentDayHighlightChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleCurrentDayHighlight) },
        streakHighlightChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleStreakHighlight) },
    )
}

@Composable
private fun GeneralContent(
    state: State<Settings>,
    weekStartChange: (Boolean) -> Unit = {},
    currentDayHighlightChange: (Boolean) -> Unit = {},
    streakHighlightChange: (Boolean) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.clip(MaterialTheme.shapes.small)
        ) {
            SwitchSetting(
                weekStartChange,
                stringResource(R.string.week_starts_on_sunday), state.value.weekStartsOnSunday.value
            )
            HorizontalDivider()
            SwitchSetting(
                currentDayHighlightChange,
                stringResource(R.string.highlight_current_day), state.value.currentDayHighlighted
            )
            HorizontalDivider()
            SwitchSetting(
                streakHighlightChange,
                stringResource(R.string.highlight_streaks), state.value.streaksHighlighted
            )
        }
    }
}

@Composable
private fun SwitchSetting(onChange: (Boolean) -> Unit, title: String, isChecked: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MenuItemText(
            text = title,
            modifier = Modifier.padding(vertical = 12.dp)
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
        GeneralContent(state = remember { mutableStateOf(Settings()) })
    }
}

