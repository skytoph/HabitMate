package com.skytoph.taski.presentation.settings.general.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skytoph.taski.R
import com.skytoph.taski.core.datastore.settings.Settings
import com.skytoph.taski.presentation.core.component.MenuItemText
import com.skytoph.taski.presentation.habit.icon.component.getActivity
import com.skytoph.taski.presentation.settings.general.GeneralSettingsEvent
import com.skytoph.taski.presentation.settings.general.GeneralSettingsState
import com.skytoph.taski.presentation.settings.general.GeneralSettingsViewModel
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun GeneralSettingsScreen(
    viewModel: GeneralSettingsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.initAppBar(title = R.string.settings_general_title)
        context.getActivity()?.let { viewModel.init(it) }
    }

    GeneralContent(
        state = viewModel.state(),
        settingsState = viewModel.settings().collectAsState(),
        weekStartChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleWeekStart) },
        currentDayHighlightChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleCurrentDayHighlight) },
        streakHighlightChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleStreakHighlight) },
        iconWarningChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleIconWarning) },
        allowCrashlytics = { viewModel.onEvent(GeneralSettingsEvent.ToggleAllowCrashlytics) },
        timeFormatChange = { viewModel.onEvent(GeneralSettingsEvent.ToggleTimeFormat) },
        privacySettings = { context.getActivity()?.let { viewModel.openPrivacySettings(it) } },
        copyId = { viewModel.copyId(context) }
    )
}

@Composable
private fun GeneralContent(
    state: State<GeneralSettingsState>,
    settingsState: State<Settings>,
    weekStartChange: (Boolean) -> Unit = {},
    currentDayHighlightChange: (Boolean) -> Unit = {},
    timeFormatChange: (Boolean) -> Unit = {},
    streakHighlightChange: (Boolean) -> Unit = {},
    iconWarningChange: (Boolean) -> Unit = {},
    allowCrashlytics: (Boolean) -> Unit = {},
    privacySettings: () -> Unit = {},
    copyId: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsTitle(
            text = stringResource(R.string.general_settings_item_view),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Column(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .clip(MaterialTheme.shapes.small)
        ) {
            SwitchSetting(
                onChange = weekStartChange,
                title = stringResource(R.string.week_starts_on_sunday),
                isChecked = settingsState.value.weekStartsOnSunday.value
            )
            HorizontalDivider()
            SwitchSetting(
                onChange = timeFormatChange,
                title = stringResource(R.string.time_format_is_24h),
                isChecked = settingsState.value.time24hoursFormat.value
            )
            HorizontalDivider()
            SwitchSetting(
                onChange = currentDayHighlightChange,
                title = stringResource(R.string.highlight_current_day),
                isChecked = settingsState.value.currentDayHighlighted
            )
            HorizontalDivider()
            SwitchSetting(
                onChange = streakHighlightChange,
                title = stringResource(R.string.highlight_streaks),
                isChecked = settingsState.value.streaksHighlighted
            )
            HorizontalDivider()
            SwitchSetting(
                onChange = iconWarningChange,
                title = stringResource(R.string.show_icon_warning),
                isChecked = settingsState.value.showIconWarning
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        SettingsTitle(
            text = stringResource(R.string.general_settings_item_crash_report),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Column(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .clip(MaterialTheme.shapes.small)
        ) {
            SwitchSetting(
                onChange = allowCrashlytics,
                title = stringResource(R.string.allow_crashlytics),
                isChecked = settingsState.value.allowCrashlytics ?: false
            )
            HorizontalDivider()
            ButtonWithIcon(
                onClick = copyId,
                title = stringResource(R.string.copy_support_id),
                color = MaterialTheme.colorScheme.onBackground,
                icon = ImageVector.vectorResource(R.drawable.copy),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        if (state.value.isPrivacySettingVisible) {
            Spacer(modifier = Modifier.height(16.dp))
            SettingsTitle(
                text = stringResource(R.string.general_settings_item_ad_preferences),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Column(
                modifier = Modifier
                    .widthIn(max = 520.dp)
                    .clip(MaterialTheme.shapes.small)
            ) {
                ButtonWithIcon(
                    onClick = privacySettings,
                    title = stringResource(R.string.privacy_settings),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SettingsTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun ButtonWithIcon(
    onClick: () -> Unit = {},
    title: String,
    color: Color,
    icon: ImageVector? = null,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = style,
            color = color,
            fontWeight = fontWeight
        )
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = color
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
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            GeneralContent(
                state = remember { mutableStateOf(GeneralSettingsState(isPrivacySettingVisible = true)) },
                settingsState = remember { mutableStateOf(Settings()) })
        }
    }
}

