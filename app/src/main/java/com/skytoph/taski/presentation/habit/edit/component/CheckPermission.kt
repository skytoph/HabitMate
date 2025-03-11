package com.skytoph.taski.presentation.habit.edit.component

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import com.skytoph.taski.core.reminder.HabitNotificationChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun checkPermission(
    context: Context,
    requestAlarmPermission: () -> Unit,
    requestNotificationPermission: () -> Unit,
    onGranted: () -> Unit,
) {
    val notificationEnabled = areNotificationsEnabled(context)
    val alarmEnabled = areAlarmsEnabled(context)

    when {
        !notificationEnabled -> requestNotificationPermission()
        !alarmEnabled -> requestAlarmPermission()
        else -> onGranted()
    }
}

fun areAlarmsEnabled(context: Context) =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
            || (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).canScheduleExactAlarms()

fun areNotificationsEnabled(context: Context) =
    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && PackageManager.PERMISSION_GRANTED == ContextCompat
        .checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS))
            || NotificationManagerCompat.from(context).areNotificationsEnabled()

fun askForNotificationPermission(
    lifecycleState: State<Lifecycle.State>,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    showDialog: () -> Unit,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
        showDialog()
    else {
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        showDialogIfPermissionNotRequested(lifecycleState, showDialog)
    }
}

private fun showDialogIfPermissionNotRequested(
    lifecycleState: State<Lifecycle.State>,
    showDialog: () -> Unit
) {
    Handler(Looper.getMainLooper()).postDelayed({
        if (lifecycleState.value === Lifecycle.State.RESUMED)
            showDialog()
    }, 400)
}

fun startNotificationSettingsActivity(
    lifecycleScope: CoroutineScope,
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    lifecycleScope.launch { launcher.launch(notificationSettingsIntent(context)) }
}

fun startAlarmSettingsActivity(
    lifecycleScope: CoroutineScope,
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    lifecycleScope.launch {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            launcher.launch(alarmSettingsIntent(context))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
fun alarmSettingsIntent(context: Context): Intent =
    Intent(
        Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
        Uri.fromParts("package", context.packageName, null)
    ).addFlags(Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT)

fun notificationSettingsIntent(context: Context): Intent =
    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        .putExtra(Settings.EXTRA_CHANNEL_ID, HabitNotificationChannel.HabitReminder.id)
        .apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                addFlags(Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT)
        }

@Composable
fun LaunchNotificationSettingsScreen(
    handleResult: (Boolean) -> Unit,
    context: Context
): ManagedActivityResultLauncher<Intent, ActivityResult> =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val notificationManager = NotificationManagerCompat.from(context)
        val areNotificationsEnabled = notificationManager.areNotificationsEnabled()
        handleResult(areNotificationsEnabled)
    }

@Composable
fun LaunchAlarmSettingsScreen(
    handleResult: (Boolean) -> Unit,
    context: Context
): ManagedActivityResultLauncher<Intent, ActivityResult> =
    rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            alarmManager.canScheduleExactAlarms()
        else true
        handleResult(alarmGranted)
    }
