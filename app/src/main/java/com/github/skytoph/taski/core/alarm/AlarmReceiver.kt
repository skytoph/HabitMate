package com.github.skytoph.taski.core.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.habit.edit.NotificationInteractor
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var interactor: NotificationInteractor

    override fun onReceive(context: Context?, intent: Intent?) {
        val item: AlarmItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent?.getSerializableExtra(AlarmItem.KEY_ITEM, AlarmItem::class.java) ?: return
        else (intent?.getSerializableExtra(AlarmItem.KEY_ITEM) ?: return) as AlarmItem

        val channelId = HabitAlarmChannel.Base.id

        context?.let {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(IconResource.Name(item.icon).id(context))
                .setContentTitle(item.title)
                .setContentText(context.getString(item.message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            notificationManager.notify(item.id, builder.build())
            interactor.rescheduleNotification(item, context)
        }
    }
}
