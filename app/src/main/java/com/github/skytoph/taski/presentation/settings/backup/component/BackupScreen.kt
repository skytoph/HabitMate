package com.github.skytoph.taski.presentation.settings.backup.component

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.reminder.RefreshRemindersReceiver
import com.github.skytoph.taski.presentation.core.component.LoadingFullscreen
import com.github.skytoph.taski.presentation.core.component.LoadingItems
import com.github.skytoph.taski.presentation.core.component.NotificationPermissionDialog
import com.github.skytoph.taski.presentation.core.component.getLocale
import com.github.skytoph.taski.presentation.habit.edit.component.RequestNotificationPermission
import com.github.skytoph.taski.presentation.settings.backup.BackupDialogUi
import com.github.skytoph.taski.presentation.settings.backup.BackupEvent
import com.github.skytoph.taski.presentation.settings.backup.BackupMessages.allowBackupMessage
import com.github.skytoph.taski.presentation.settings.backup.BackupViewModel
import com.github.skytoph.taski.presentation.settings.backup.ProfileUi
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun BackupScreen(viewModel: BackupViewModel = hiltViewModel(), restoreBackup: () -> Unit) {
    val context = LocalContext.current
    val locale = getLocale()
    val state = viewModel.state()
    val contract = ActivityResultContracts.StartActivityForResult()
    val settings = viewModel.settings().collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkConnection(context)
        viewModel.loadProfile(context)
    }

    val launcherImport = rememberLauncherForActivityResult(contract = contract) { result ->
        if (result.resultCode == Activity.RESULT_OK)
            result.data?.data?.let { uri ->
                viewModel.onEvent(BackupEvent.ImportLoading(true))
                viewModel.import(context.contentResolver, uri, context)
            }
    }
    val authorizeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK)
                viewModel.authorizeResult(context, result.data)
            else viewModel.signInFailed(result)
        }
    )

    LaunchedEffect(Unit) {
        viewModel.initAppBar(title = R.string.settings_backup_title)
    }

    LaunchedEffect(state.value.uriShareFile) {
        if (state.value.uriShareFile != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = context.getString(R.string.backup_mimetype)
            intent.putExtra(Intent.EXTRA_STREAM, state.value.uriShareFile)
            viewModel.onEvent(BackupEvent.ShareFile())
            context.startActivity(intent)
        }
    }

    LaunchedEffect(state.value.refreshingReminders) {
        if (state.value.refreshingReminders) {
            viewModel.onEvent(BackupEvent.RefreshingReminders(false))
            context.applicationContext.sendBroadcast(Intent(context, RefreshRemindersReceiver::class.java))
        }
    }

    LaunchedEffect(state.value.requestBackupPermission) {
        if (state.value.requestBackupPermission)
            viewModel.backupPermissionRequest(context = context,
                requestPermission = { authorizeLauncher.launch(IntentSenderRequest.Builder(it).build()) })
    }

    Backup(
        isImportLoading = state.value.isImportLoading,
        isExportLoading = state.value.isExportLoading,
        isClearingLoading = state.value.isClearingLoading,
        isDriveBackupLoading = state.value.isDriveBackupLoading,
        isProfileLoading = state.value.isProfileLoading,
        isSigningInLoading = state.value.isSigningInLoading,
        isInternetConnected = state.value.isInternetConnected,
        restoreBackup = restoreBackup,
        profile = state.value.profile,
        lastTimeBackupSaved = viewModel.mapBackupTime(
            state.value.lastBackupSavedTime, context, locale, settings.value.time24hoursFormat.value
        ),
        logIn = { viewModel.signIn(context = context) },
        saveBackup = { viewModel.saveBackupOnDrive(context) },
        showBackupError = { viewModel.showMessage(allowBackupMessage) },
        allowBackup = { viewModel.onEvent(BackupEvent.RequestBackupPermission(true)) },
        export = { viewModel.onEvent(BackupEvent.UpdateDialog(BackupDialogUi.Export)) },
        import = { viewModel.onEvent(BackupEvent.UpdateDialog(BackupDialogUi.Import)) },
        clear = { viewModel.onEvent(BackupEvent.UpdateDialog(BackupDialogUi.Clear)) },
        signOut = { viewModel.onEvent(BackupEvent.UpdateDialog(BackupDialogUi.SignOut)) },
        deleteAccount = { viewModel.onEvent(BackupEvent.UpdateDialog(BackupDialogUi.DeleteAccount)) }
    )

    state.value.dialog?.let { dialog ->
        BackupDialog(
            dialog = dialog,
            export = { viewModel.export(context) },
            import = { launcherImport.launch(Intent(Intent.ACTION_GET_CONTENT).apply { type = "*/*" }) },
            clear = { viewModel.clearLocalData() },
            signOut = { viewModel.signOut(context) },
            deleteAccount = { viewModel.deleteAccount(context) },
            requestPermissions = { viewModel.onEvent(BackupEvent.RequestPermissions(true)) },
            dismiss = { viewModel.onEvent(BackupEvent.UpdateDialog()) },
            dismissPermissions = { viewModel.onEvent(BackupEvent.UpdateDialog()) })
    }

    state.value.permissionDialog?.let { dialog ->
        NotificationPermissionDialog(dialog)
    }

    RequestNotificationPermission(
        requestPermissionDialog = { viewModel.onEvent(BackupEvent.UpdatePermissionDialog(it)) },
        permissionGranted = { isGranted ->
            if (isGranted) viewModel.onEvent(BackupEvent.RefreshingReminders(true))
        },
        initialize = false,
        content = { requestPermission ->
            if (state.value.requestingPermission) {
                viewModel.onEvent(BackupEvent.RequestPermissions(false))
                requestPermission()
            }
        }
    )
}

@Composable
private fun Backup(
    export: () -> Unit = {},
    import: () -> Unit = {},
    clear: () -> Unit = {},
    signOut: () -> Unit = {},
    deleteAccount: () -> Unit = {},
    showBackupError: () -> Unit = {},
    logIn: () -> Unit = {},
    saveBackup: () -> Unit = {},
    restoreBackup: () -> Unit = {},
    allowBackup: () -> Unit = {},
    isImportLoading: Boolean = false,
    isExportLoading: Boolean = true,
    isClearingLoading: Boolean = false,
    isDriveBackupLoading: Boolean = false,
    isProfileLoading: Boolean = false,
    isSigningInLoading: Boolean = false,
    isLoadingFullscreen: Boolean = false,
    isInternetConnected: Boolean = true,
//    profile: ProfileUi? = null,
    profile: ProfileUi? = ProfileUi(email = "email@gmail.com", name = "Name"),
    lastTimeBackupSaved: String? = "28.09.24 12:00",
) {
    val enabled =
        remember { mutableStateOf(!(isProfileLoading || isExportLoading || isImportLoading || isSigningInLoading || isDriveBackupLoading)) }

    LaunchedEffect(isProfileLoading, isExportLoading, isImportLoading, isSigningInLoading, isDriveBackupLoading) {
        enabled.value =
            !(isProfileLoading || isExportLoading || isImportLoading || isSigningInLoading || isDriveBackupLoading)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .widthIn(max = 520.dp)
        ) {
            Text(
                text = stringResource(R.string.google_drive_backup),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                when {
                    !isInternetConnected ->
                        Text(
                            text = stringResource(R.string.backup_no_connection),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                    isProfileLoading ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 278.dp)
                        ) {
                            LoadingItems()
                        }

                    profile == null || profile.isAnonymous || profile.isEmpty ->
                        LogInItem(
                            logIn = logIn,
                            isLoading = isSigningInLoading,
                            enabled = enabled.value
                        )

                    else ->
                        DriveBackup(
                            profile = profile,
                            lastTimeBackupSaved = lastTimeBackupSaved,
                            createBackup = saveBackup,
                            restoreBackup = restoreBackup,
                            allowBackup = allowBackup,
                            signOut = signOut,
                            isBackupLoading = isDriveBackupLoading,
                            deleteAccount = deleteAccount,
                            showBackupError = showBackupError,
                            enabled = enabled.value
                        )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.local_backup),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            LocalBackup(
                enabled = enabled.value,
                isImportLoading = isImportLoading,
                isExportLoading = isExportLoading,
                isClearingLoading = isClearingLoading,
                import = import,
                export = export,
                clear = clear
            )
        }
        AnimatedVisibility(visible = isLoadingFullscreen) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                LoadingFullscreen()
            }
        }
    }
}

@Composable
private fun LocalBackup(
    enabled: Boolean,
    isImportLoading: Boolean,
    isExportLoading: Boolean,
    isClearingLoading: Boolean,
    import: () -> Unit,
    export: () -> Unit,
    clear: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
    ) {
        ButtonWithLoadingFull(
            title = stringResource(R.string.export_title),
            onClick = export,
            isLoading = isExportLoading,
            loadingText = stringResource(R.string.loading_export),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
        HorizontalDivider()
        ButtonWithLoadingFull(
            title = stringResource(R.string.import_title),
            onClick = import,
            isLoading = isImportLoading,
            loadingText = stringResource(R.string.loading_import),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
        HorizontalDivider()
        ButtonWithLoadingFull(
            title = stringResource(R.string.clear_all_data),
            onClick = clear,
            isLoading = isClearingLoading,
            loadingText = stringResource(R.string.loading_clearing),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            textColor = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun DriveBackup(
    profile: ProfileUi,
    createBackup: () -> Unit,
    allowBackup: () -> Unit,
    restoreBackup: () -> Unit,
    signOut: () -> Unit = {},
    deleteAccount: () -> Unit,
    showBackupError: () -> Unit,
    isBackupLoading: Boolean,
    lastTimeBackupSaved: String?,
    enabled: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        MaterialTheme.shapes.small
                    )
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                ProfileItem(profile = profile, lastTimeBackupSaved = lastTimeBackupSaved)
                Spacer(modifier = Modifier.height(8.dp))
                if (profile.isBackupAvailable) ButtonWithLoading(
                    title = stringResource(R.string.create_backup_on_drive),
                    onClick = createBackup,
                    isLoading = isBackupLoading,
                    loadingText = stringResource(R.string.loading_backup),
                    enabled = enabled,
                    textPadding = 16.dp
                )
                else ButtonWithLoading(
                    title = stringResource(R.string.allow_backup),
                    onClick = allowBackup,
                    isLoading = isBackupLoading,
                    loadingText = stringResource(R.string.loading_backup_permission),
                    enabled = enabled,
                    textPadding = 16.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
        ) {
            ButtonWithLoadingFull(
                title = stringResource(R.string.restore_item),
                onClick = if (profile.isBackupAvailable) restoreBackup else showBackupError,
                isLoading = false,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            )
            HorizontalDivider()
            ButtonWithLoadingFull(
                title = stringResource(R.string.delete_account_item),
                onClick = deleteAccount,
                textColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            )
            HorizontalDivider()
            ButtonWithLoadingFull(
                title = stringResource(R.string.sign_out_item),
                onClick = signOut,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled
            )
        }
    }
}

@Composable
private fun ProfileItem(profile: ProfileUi, lastTimeBackupSaved: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profile.imageUri)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.circle_user_round)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.width(IntrinsicSize.Max)) {
            Text(
                text = profile.email,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.icons_are_synchronized),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light)
            )
            if (profile.isBackupAvailable) {
                Spacer(modifier = Modifier.height(2.dp))
                Crossfade(
                    targetState = lastTimeBackupSaved == null,
                    label = "loading_backup_time_crossfade",
                    animationSpec = tween(durationMillis = 150),
                    modifier = Modifier
                        .heightIn(min = 16.dp)
                        .fillMaxWidth(),
                ) {
                    if (it) Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        LoadingItems(
                            itemColor = MaterialTheme.colorScheme.primary,
                            item = ImageVector.vectorResource(id = R.drawable.sparkle_filled),
                        )
                    } else Text(
                        text = lastTimeBackupSaved!!,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light)
                    )
                }
            }
        }
    }
}

@Composable
fun LogInItem(
    logIn: () -> Unit,
    isLoading: Boolean,
    enabled: Boolean,
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.backup_with_google_description),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonWithLoading(
            title = stringResource(R.string.log_in),
            onClick = logIn,
            isLoading = isLoading,
            loadingText = stringResource(R.string.signing_in),
            enabledColor = MaterialTheme.colorScheme.primary,
            disabledColor = MaterialTheme.colorScheme.secondaryContainer,
            enabled = enabled,
            textPadding = 64.dp
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
    isLoading: Boolean,
    tint: Color? = null
) {
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
                tint = tint ?: LocalContentColor.current,
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
        ButtonWithLoading(
            title = title,
            onClick = onClick,
            isLoading = isLoading,
            loadingText = loadingText,
            enabledColor = MaterialTheme.colorScheme.onTertiaryContainer,
            disabledColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )
    }
}

@Composable
fun ButtonWithLoading(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    loadingText: String = "",
    enabledColor: Color = MaterialTheme.colorScheme.primary,
    disabledColor: Color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f),
    style: TextStyle = MaterialTheme.typography.titleSmall,
    shape: Shape = MaterialTheme.shapes.large,
    textColor: Color = Color.White,
    enabled: Boolean,
    textPadding: Dp = 8.dp
) {
    val color = remember { Animatable(if (isLoading) disabledColor else enabledColor) }
    LaunchedEffect(isLoading) {
        color.animateTo(
            targetValue = if (isLoading) disabledColor else enabledColor,
            animationSpec = tween(durationMillis = 400)
        )
    }
    Box(modifier = modifier
        .background(color = color.value, shape = shape)
        .clip(MaterialTheme.shapes.small)
        .clickable(enabled = !isLoading && enabled) { onClick() }
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .animateContentSize(),
        contentAlignment = Alignment.Center) {
        Crossfade(
            targetState = isLoading,
            label = "backup_button_crossfade",
            animationSpec = tween(durationMillis = 150)
        ) { load ->
            Column(
                modifier = modifier.padding(horizontal = textPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (load) loadingText else title,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    style = style
                )
                if (load) LoadingItems(spaceSize = 4.dp)
            }
        }
    }
}

@Composable
private fun ButtonWithLoadingFull(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    loadingText: String = "",
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    enabled: Boolean
) {
    val disabled = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    val enabledColor = MaterialTheme.colorScheme.primaryContainer
    val color = remember { Animatable(if (isLoading) disabled else enabledColor) }
    LaunchedEffect(isLoading) {
        color.animateTo(
            targetValue = if (isLoading) disabled else enabledColor,
            animationSpec = tween(durationMillis = 400)
        )
    }
    Box(modifier = modifier
        .background(color = color.value)
        .clickable(enabled = !isLoading && enabled) { onClick() }
        .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center) {
        Crossfade(
            targetState = isLoading,
            label = "backup_button_crossfade",
            animationSpec = tween(durationMillis = 400)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = if (it) loadingText else title,
                    style = MaterialTheme.typography.titleSmall,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                if (it) LoadingItems(spaceSize = 4.dp)
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