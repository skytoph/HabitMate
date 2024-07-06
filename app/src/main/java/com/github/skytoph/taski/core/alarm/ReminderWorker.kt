package com.github.skytoph.taski.core.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.skytoph.taski.MainActivity
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.habit.edit.NotificationInteractor
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    private val interactor: NotificationInteractor,
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val item: AlarmItem =
            Gson().fromJson(workerParams.inputData.getString(KEY_ITEM), AlarmItem::class.java)

        val channelId = HabitAlarmChannel.Base.id

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(IconResource.Name(item.icon).id(context))
            .setColor(item.color)
            .setContentTitle(item.title)
            .setContentText(context.getString(item.message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        notificationManager.notify(item.id.toInt(), builder.build())
        interactor.rescheduleNotification(item, context)
        return Result.success()
    }

    companion object {
        const val KEY_ITEM = "key_item"
    }
}