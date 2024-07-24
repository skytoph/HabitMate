package com.github.skytoph.taski.presentation.settings.backup.component

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.appbar.SnackbarMessage
import com.github.skytoph.taski.presentation.core.component.LoadingItems
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.settings.backup.BackupEvent
import com.github.skytoph.taski.presentation.settings.backup.BackupViewModel
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun BackupScreen(viewModel: BackupViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val state = viewModel.state()
    val contract = ActivityResultContracts.StartActivityForResult()
    val successfulImport by lazy {
        SnackbarMessage(
            message = context.getString(R.string.success_import_message),
            title = context.getString(R.string.success_import_title),
            icon = IconResource.Id(id = R.drawable.folder_input)
        )
    }
    val errorImport by lazy {
        SnackbarMessage(
            message = context.getString(R.string.error_import),
            title = context.getString(R.string.import_error_title),
            icon = IconResource.Id(id = R.drawable.folder_input)
        )
    }
    val errorExport by lazy {
        SnackbarMessage(
            message = context.getString(R.string.error_export),
            title = context.getString(R.string.error_export_title),
            icon = IconResource.Id(id = R.drawable.folder_output)
        )
    }

    val launcherImport = rememberLauncherForActivityResult(contract = contract) { result ->
        if (result.resultCode == Activity.RESULT_OK)
            result.data?.data?.let { uri ->
                viewModel.import(context.contentResolver, uri, successfulImport, errorImport)
            }
    }

    LaunchedEffect(Unit) {
        viewModel.initAppBar(title = R.string.settings_backup_title)
    }

    LaunchedEffect(state.value.uri) {
        if (state.value.uri != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/octet-stream"
            intent.putExtra(Intent.EXTRA_STREAM, state.value.uri)
            viewModel.onEvent(BackupEvent.ShareFile())
            context.startActivity(intent)
        }
    }

    Backup(
        isImportLoading = state.value.importLoading,
        isExportLoading = state.value.exportLoading,
        export = { viewModel.export(context, errorExport) },
        import = { launcherImport.launch(Intent(Intent.ACTION_GET_CONTENT).apply { type = "*/*" }) })
}

@Composable
private fun Backup(
    export: () -> Unit = {},
    import: () -> Unit = {},
    isImportLoading: Boolean = true,
    isExportLoading: Boolean = true,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        BackupItem(
            onClick = export,
            title = stringResource(R.string.export_title),
            text = stringResource(R.string.export_description),
            loadingText = "Preparing the data...",
            icon = ImageVector.vectorResource(R.drawable.folder_output),
            isLoading = isExportLoading
        )
        BackupItem(
            onClick = import,
            title = stringResource(R.string.import_title),
            text = stringResource(R.string.import_description),
            loadingText = "Restoring the data...",
            icon = ImageVector.vectorResource(R.drawable.folder_input),
            isLoading = isImportLoading
        )
    }
}

@Composable
private fun BackupItem(
    onClick: () -> Unit,
    title: String,
    text: String,
    loadingText: String,
    icon: ImageVector,
    isLoading: Boolean
) {
    val enabled = MaterialTheme.colorScheme.primary
    val disabled = MaterialTheme.colorScheme.secondaryContainer
    val color = remember { Animatable(if (isLoading) disabled else enabled) }
    LaunchedEffect(isLoading) {
        color.animateTo(if (isLoading) disabled else enabled)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Crossfade(targetState = isLoading, label = "loading_crossfade", animationSpec = tween(durationMillis = 150)) {
            if (it) LoadingItems(
                itemColor = MaterialTheme.colorScheme.primary,
                item = ImageVector.vectorResource(id = R.drawable.sparkle_filled)
            ) else Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .background(
                color = color.value,
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .clickable(enabled = !isLoading) { onClick() }
            .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center) {
            Crossfade(
                targetState = isLoading,
                label = "backup_button_crossfade",
                animationSpec = tween(durationMillis = 150)
            ) {
                Text(
                    text = if (it) loadingText else title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview
fun BackupPreview() {
    HabitMateTheme(darkTheme = true) {
        Box(modifier = Modifier.background(Color.Black)) { Backup() }
    }
}