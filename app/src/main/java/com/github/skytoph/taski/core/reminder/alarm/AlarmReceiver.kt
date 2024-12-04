package com.github.skytoph.taski.core.reminder.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.github.skytoph.taski.MainActivity
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.AsyncBroadcastReceiver
import com.github.skytoph.taski.core.reminder.HabitNotificationChannel
import com.github.skytoph.taski.core.reminder.ReminderItem
import com.github.skytoph.taski.presentation.core.state.StringResource
import com.github.skytoph.taski.presentation.habit.edit.NotificationStateInteractor
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : AsyncBroadcastReceiver() {

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var interactor: NotificationStateInteractor

    override fun onReceive(context: Context, intent: Intent) = goAsync {
        val stringExtra = intent.getStringExtra(ReminderItem.KEY_ITEM) ?: return@goAsync
        val item: ReminderItem = gson.fromJson(stringExtra, ReminderItem::class.java)

        context.let {
            if (interactor.notCompleted(item.id)) {
                val habit = interactor.habit(item.id)
                val channelId = HabitNotificationChannel.HabitReminder.id

                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val activityIntent = Intent(context, MainActivity::class.java)
                val pendingIntent: PendingIntent =
                    PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE)

                val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.sparkles)
                    .setColor(habit.color)
                    .setSilent(true)
                    .setContentTitle(habit.title)
                    .setContentText(StringResource.Identifier(item.messageIdentifier).getString(context))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                notificationManager.notify(item.id.toInt(), builder.build())
            }
            interactor.rescheduleNotification(item, context)
        }
    }

    companion object {
        const val ACTION = "com.github.skytoph.habitmate.NOTIFY"
    }
}

