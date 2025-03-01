package com.github.skytoph.taski.core.reminder.worker

import android.content.Context
import android.content.pm.ServiceInfo
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.reminder.CreateNotificationChannel
import com.github.skytoph.taski.core.reminder.HabitNotificationChannel
import com.github.skytoph.taski.presentation.habit.edit.NotificationInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class ReminderRefreshWorker @AssistedInject constructor(
    private val interactor: NotificationInteractor,
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), CreateNotificationChannel by CreateNotificationChannel.Base() {

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())
        delay(4000)
        interactor.refreshAllNotifications(context)
        return Result.success()
    }

    private fun createForegroundInfo(): ForegroundInfo {
        createChannel(context, HabitNotificationChannel.RefreshReminder)
        val notification = NotificationCompat.Builder(context, HabitNotificationChannel.RefreshReminder.id)
            .setContentTitle(context.getString(R.string.notification_reschedule_title))
            .setSmallIcon(R.drawable.sparkle)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground))
            .setOngoing(true)
            .setProgress(100, 0, true)
            .build()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            ForegroundInfo(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        else
            ForegroundInfo(NOTIFICATION_ID, notification)
    }

    companion object {
        const val NOTIFICATION_ID = 1001
    }
}