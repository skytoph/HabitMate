package com.github.skytoph.taski.core.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.github.skytoph.taski.presentation.core.state.IconResource

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getIntExtra(AlarmItem.KEY_MESSAGE, 0)
            ?.also { if (it == 0) return } ?: return
        val title = intent.getStringExtra(AlarmItem.KEY_TITLE) ?: return
        val icon = intent.getStringExtra(AlarmItem.KEY_ICON) ?: return
        val channelId = "alarm_id"
        context?.let { ctx ->
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(IconResource.Name(icon).id(context))
                .setContentTitle(title)
                .setContentText(context.getString(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(1, builder.build())
        }
    }
}
