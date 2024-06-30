package com.github.skytoph.taski.core.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.github.skytoph.taski.domain.habit.IsHabitDone
import com.github.skytoph.taski.presentation.core.state.IconResource
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: IsHabitDone

    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getLongExtra(AlarmItem.KEY_ID, -1L)
            ?.also { if (it == -1L) return } ?: return
        if (repository.isHabitDone(id)) return
        val message = intent.getIntExtra(AlarmItem.KEY_MESSAGE, 0)
            .also { if (it == 0) return }
        val title = intent.getStringExtra(AlarmItem.KEY_TITLE) ?: return
        val icon = intent.getStringExtra(AlarmItem.KEY_ICON) ?: return
        val channelId = "alarm_id"
        context?.let {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(IconResource.Name(icon).id(context))
                .setContentTitle(title)
                .setContentText(context.getString(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(1, builder.build())
        }
    }
}
