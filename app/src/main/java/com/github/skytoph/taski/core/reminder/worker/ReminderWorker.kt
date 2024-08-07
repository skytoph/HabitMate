package com.github.skytoph.taski.core.reminder.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.skytoph.taski.MainActivity
import com.github.skytoph.taski.core.reminder.HabitNotificationChannel
import com.github.skytoph.taski.core.reminder.ReminderItem
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.core.state.StringResource
import com.github.skytoph.taski.presentation.habit.edit.NotificationStateInteractor
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    private val interactor: NotificationStateInteractor,
    private val gson: Gson,
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val item: ReminderItem = gson.fromJson(workerParams.inputData.getString(ReminderItem.KEY_ITEM), ReminderItem::class.java)

        if (interactor.notCompleted(item.id)) {
            val habit = interactor.habit(item.id)
            val channelId = HabitNotificationChannel.HabitReminder.id

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(IconResource.Name(habit.iconName).id(context))
                .setColor(habit.color)
                .setContentTitle(habit.title)
                .setContentText(StringResource.Identifier(item.messageIdentifier).getString(context))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            notificationManager.notify(item.id.toInt(), builder.build())
        }
        interactor.rescheduleNotification(item, context)
        return Result.success()
    }
}