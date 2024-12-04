package com.github.skytoph.taski.presentation.settings.menu.component

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
    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        MenuTitleText(text = "App", modifier = Modifier.padding(start = 16.dp))
        Column(modifier = Modifier.clip(MaterialTheme.shapes.small)) {
            SettingsMenuItem(
                title = "General",
                icon = ImageVector.vectorResource(id = R.drawable.wrench),
                onClick = generalClick,
                color = Blue
            )
            HorizontalDivider()
            SettingsMenuItem(
                title = "Theme",
                icon = ImageVector.vectorResource(id = R.drawable.palette),
                onClick = themeClick,
                color = Green
            )
            HorizontalDivider()
            SettingsMenuItem(
                title = "Reorder habits",
                icon = ImageVector.vectorResource(id = R.drawable.arrow_up_down),
                onClick = reorderClick,
                color = Yellow
            )
            HorizontalDivider()
            SettingsMenuItem(
                title = "Archived habits",
                icon = ImageVector.vectorResource(id = R.drawable.archive),
                onClick = archiveClick,
                color = Orange
            )
            HorizontalDivider()
            SettingsMenuItem(
                title = "Backup",
                icon = ImageVector.vectorResource(id = R.drawable.folder_input),
                onClick = backupClick,
                color = Red
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        MenuTitleText(text = "Info", modifier = Modifier.padding(start = 16.dp))
        Column(modifier = Modifier.clip(MaterialTheme.shapes.small)) {
            SettingsMenuItem(
                title = "Privacy policy",
                icon = ImageVector.vectorResource(id = R.drawable.file_lock),
                onClick = {},
                color = GrayLight
            )
            HorizontalDivider()
            SettingsMenuItem(
                title = "Terms of use",
                icon = ImageVector.vectorResource(id = R.drawable.file_type),
                onClick = {},
                color = PurpleLight
            )
            HorizontalDivider()
            SettingsMenuItem(
                title = "Credits",
                icon = ImageVector.vectorResource(id = R.drawable.file_heart),
                onClick = creditsClick,
                color = PinkLight
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        MenuTitleText(text = "Feedback", modifier = Modifier.padding(start = 16.dp))
        Column(modifier = Modifier.clip(MaterialTheme.shapes.small)) {
            SettingsMenuItem(
                title = "Rate the app",
                icon = ImageVector.vectorResource(id = R.drawable.star),
                onClick = {},
                color = Peach
            )
            HorizontalDivider()
            SettingsMenuItem(
                title = "Share the app",
                icon = ImageVector.vectorResource(id = R.drawable.share_2),
                onClick = {},
                color = BlueLight
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "HabitMate 1.0.0",
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