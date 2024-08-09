package com.github.skytoph.taski.core.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.github.skytoph.taski.core.reminder.worker.ReminderRefreshWorker

class RefreshRemindersReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val workManager = WorkManager.getInstance(context)
        val request = OneTimeWorkRequest.Builder(ReminderRefreshWorker::class.java)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
        workManager.enqueue(request)
    }

    companion object {
        const val ACTION = "com.github.skytoph.habitmate.REFRESH_REMINDERS"
    }
}