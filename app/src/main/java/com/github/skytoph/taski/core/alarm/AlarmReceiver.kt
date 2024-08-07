package com.github.skytoph.taski.core.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.github.skytoph.taski.MainActivity
import com.github.skytoph.taski.presentation.core.state.StringResource
import com.github.skytoph.taski.presentation.habit.edit.NotificationStateInteractor
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var interactor: NotificationStateInteractor

    override fun onReceive(context: Context?, intent: Intent?) {
        val item: AlarmItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent?.getSerializableExtra(AlarmItem.KEY_ITEM, AlarmItem::class.java) ?: return
        else (intent?.getSerializableExtra(AlarmItem.KEY_ITEM) ?: return) as AlarmItem

        val channelId = HabitNotificationChannel.HabitReminder.id

        context?.let {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(context, channelId)
                .setContentText(StringResource.Identifier(item.messageIdentifier).getString(context))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
            notificationManager.notify(item.id.toInt(), builder.build())
            interactor.rescheduleNotification(item, context,)
        }
    }

    companion object {
        const val ACTION = "com.github.skytoph.habitmate.NOTIFY"
    }
}

