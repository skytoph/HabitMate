package com.github.skytoph.taski.presentation.settings.menu.component

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.MenuTitleText
import com.github.skytoph.taski.presentation.core.component.TitleWithIconMenuItem
import com.github.skytoph.taski.presentation.settings.menu.SettingsMenuViewModel
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun SettingsMenuScreen(
    viewModel: SettingsMenuViewModel = hiltViewModel(),
    archiveClick: () -> Unit,
    reorderClick: () -> Unit,
    generalClick: () -> Unit,
    themeClick: () -> Unit,
    backupClick: () -> Unit,
    creditsClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.initAppBar(title = R.string.settings_title)
    }

    SettingsMenu(
        generalClick = generalClick,
        reorderClick = reorderClick,
        archiveClick = archiveClick,
        themeClick = themeClick,
        backupClick = backupClick,
        creditsClick = creditsClick
    )
}

@Composable
private fun SettingsMenu(
    generalClick: () -> Unit = {},
    reorderClick: () -> Unit = {},
    archiveClick: () -> Unit = {},
    themeClick: () -> Unit = {},
    backupClick: () -> Unit = {},
    creditsClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .verticalScroll(state = rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MenuTitleText(text = stringResource(R.string.settings_app), modifier = Modifier.padding(start = 16.dp))
        Column(modifier = Modifier.clip(MaterialTheme.shapes.small)) {
            SettingsMenuItem(
                title = stringResource(R.string.settings_general),
                icon = ImageVector.vectorResource(id = R.drawable.wrench),
                onClick = generalClick,
                color = Blue
            )
            HorizontalDivider()
            SettingsMenuItem(
                title = stringResource(R.string.settings_theme),
                icon = ImageVector.vectorResource(id = R.drawable.palette),
                onClick = themeClick,
                color = Green
            )
            HorizontalDivider()
            SettingsMenuItem(
                title = stringResource(R.string.settings_reorder_habits),
                icon = ImageVector.vectorResource(id = R.drawable.arrow_up_down),
                onClick = reorderClick,
                color = Yellow
            )
            HorizontalDivider()
            SettingsMenuItem(
                title = stringResource(R.string.settings_archived_habits),
                icon = ImageVector.vectorResource(id = R.drawable.archive),
                onClick = archiveClick,
                color = Orange
            )
            HorizontalDivider()
            SettingsMenuItem(
                title = stringResource(R.string.settings_data_backup),
                icon = ImageVector.vectorResource(id = R.drawable.folder_input),
                onClick = backupClick,
                color = Red
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        MenuTitleText(text = stringResource(R.string.settings_info), modifier = Modifier.padding(start = 16.dp))
        Column(modifier = Modifier.clip(MaterialTheme.shapes.small)) {
            SettingsLinkMenuItem(
                title = stringResource(R.string.settings_privacy_policy),
                icon = ImageVector.vectorResource(id = R.drawable.file_lock),
                color = GrayLight,
                url = stringResource(R.string.privacy_policy_url)
            )
            HorizontalDivider()
            SettingsLinkMenuItem(
                title = stringResource(R.string.settings_terms_of_use),
                icon = ImageVector.vectorResource(id = R.drawable.file_type),
                color = PurpleLight,
                url = stringResource(R.string.terms_of_use_url)
            )
            HorizontalDivider()
            SettingsMenuItem(
                title = stringResource(R.string.settings_credits),
                icon = ImageVector.vectorResource(id = R.drawable.file_heart),
                onClick = creditsClick,
                color = PinkLight
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        MenuTitleText(text = stringResource(R.string.settings_feedback), modifier = Modifier.padding(start = 16.dp))
        Column(modifier = Modifier.clip(MaterialTheme.shapes.small)) {
            SettingsLinkMenuItem(
                title = stringResource(R.string.settings_rate_the_app),
                icon = ImageVector.vectorResource(id = R.drawable.star),
                color = Peach,
                url = stringResource(R.string.app_page_url)
            )
            HorizontalDivider()
            SettingsShareMenuItem(
                title = stringResource(R.string.settings_share_the_app),
                icon = ImageVector.vectorResource(id = R.drawable.share_2),
                color = BlueLight,
                message = stringResource(R.string.share_app_message) + stringResource(R.string.new_line) +
                        stringResource(R.string.app_page_url),
                messageTitle = stringResource(R.string.share_app_title)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.settings_app_version),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.heart),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun SettingsMenuItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    color: Color,
) {
    TitleWithIconMenuItem(
        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
        title = title,
        icon = icon,
        onClick = onClick,
        tint = color,
    )
}

@Composable
private fun SettingsLinkMenuItem(
    title: String,
    icon: ImageVector,
    color: Color,
    url: String
) {
    val uriHandler = LocalUriHandler.current
    TitleWithIconMenuItem(
        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
        title = title,
        icon = icon,
        onClick = { uriHandler.openUri(url) },
        tint = color,
    )
}

@Composable
private fun SettingsShareMenuItem(
    title: String,
    icon: ImageVector,
    color: Color,
    message: String,
    messageTitle: String,
) {
    val context = LocalContext.current
    val sendIntent: Intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, messageTitle)
    TitleWithIconMenuItem(
        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
        title = title,
        icon = icon,
        onClick = { context.startActivity(shareIntent) },
        tint = color,
    )
}

@Composable
@Preview(showBackground = true)
private fun DarkSettingsPreview() {
    HabitMateTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            SettingsMenu()
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SettingsPreview() {
    HabitMateTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            SettingsMenu()
        }
    }
}