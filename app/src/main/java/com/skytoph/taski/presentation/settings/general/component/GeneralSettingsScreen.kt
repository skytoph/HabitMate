package com.skytoph.taski.presentation.settings.general.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import com.skytoph.taski.R
import com.skytoph.taski.core.datastore.settings.Settings
import com.skytoph.taski.presentation.core.component.MenuItemText
import com.skytoph.taski.presentation.settings.general.GeneralSettingsEvent
import com.skytoph.taski.presentation.settings.general.GeneralSettingsViewModel
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun GeneralSettingsScreen(
    viewModel: GeneralSettingsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.initAppBar(title = R.string.settings_general_title)
    }

    GeneralContent(
        state = viewModel.settings().collectAsState(),
        weekStartChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleWeekStart) },
        currentDayHighlightChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleCurrentDayHighlight) },
        streakHighlightChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleStreakHighlight) },
        iconWarningChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleIconWarning) },
        allowCrashlytics = { viewModel.onEvent(GeneralSettingsEvent.ToggleAllowCrashlytics) },
        timeFormatChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleTimeFormat) },
    )
}

@Composable
private fun GeneralContent(
    state: State<Settings>,
    weekStartChange: (Boolean) -> Unit = {},
    currentDayHighlightChange: (Boolean) -> Unit = {},
    timeFormatChange: (Boolean) -> Unit = {},
    streakHighlightChange: (Boolean) -> Unit = {},
    iconWarningChange: (Boolean) -> Unit = {},
    allowCrashlytics: (Boolean) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .clip(MaterialTheme.shapes.small)
        ) {
            SwitchSetting(
                onChange = weekStartChange,
                title = stringResource(R.string.week_starts_on_sunday),
                isChecked = state.value.weekStartsOnSunday.value
            )
            HorizontalDivider()
            SwitchSetting(
                onChange = timeFormatChange,
                title = stringResource(R.string.time_format_is_24h),
                isChecked = state.value.time24hoursFormat.value
            )
            HorizontalDivider()
            SwitchSetting(
                onChange = currentDayHighlightChange,
                title = stringResource(R.string.highlight_current_day),
                isChecked = state.value.currentDayHighlighted
            )
            HorizontalDivider()
            SwitchSetting(
                onChange = streakHighlightChange,
                title = stringResource(R.string.highlight_streaks),
                isChecked = state.value.streaksHighlighted
            )
            HorizontalDivider()
            SwitchSetting(
                onChange = iconWarningChange,
                title = stringResource(R.string.show_icon_warning),
                isChecked = state.value.showIconWarning
            )
            HorizontalDivider()
            SwitchSetting(
                onChange = allowCrashlytics,
                title = stringResource(R.string.allow_crashlytics),
                isChecked = state.value.allowCrashlytics ?: false
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
            modifier = Modifier
                .padding(vertical = 12.dp)
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
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
@Preview(locale = "uk")
private fun GeneralSettingsScreenPreview() {
    HabitMateTheme(darkTheme = true) {
        GeneralContent(state = remember { mutableStateOf(Settings()) })
    }
}

