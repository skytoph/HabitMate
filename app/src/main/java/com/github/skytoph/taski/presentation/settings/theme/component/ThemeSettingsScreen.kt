package com.github.skytoph.taski.presentation.settings.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.datastore.Settings
import com.github.skytoph.taski.presentation.core.component.MenuOptionComponent
import com.github.skytoph.taski.presentation.core.component.MenuTitleText
import com.github.skytoph.taski.presentation.settings.theme.AppTheme
import com.github.skytoph.taski.presentation.settings.theme.ThemeSettingsEvent
import com.github.skytoph.taski.presentation.settings.theme.ThemeSettingsViewModel
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun ThemeSettingsScreen(
    viewModel: ThemeSettingsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.initAppBar(title = R.string.settings_theme_title)
    }

    ThemeSettings(
        state = viewModel.settings().collectAsState(),
        selectTheme = { viewModel.onEvent(ThemeSettingsEvent.SelectTheme(it)) }
    )
}

@Composable
private fun ThemeSettings(
    state: State<Settings>,
    selectTheme: (AppTheme) -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        MenuTitleText(text = stringResource(R.string.settings_theme_mode))
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            val context = LocalContext.current
            AppTheme.values.forEachIndexed { index, item ->
                MenuOptionComponent(
                    option = item,
                    selected = state.value.theme == item,
                    select = selectTheme,
                    title = item.name.getString(context)
                )
                if (index < AppTheme.values.size - 1) HorizontalDivider()
            }
        }
    }
}

@Composable
@Preview
private fun ThemeSettingsScreenPreview() {
    HabitMateTheme(darkTheme = true) {
        ThemeSettings(state = remember { mutableStateOf(Settings()) })
    }
}