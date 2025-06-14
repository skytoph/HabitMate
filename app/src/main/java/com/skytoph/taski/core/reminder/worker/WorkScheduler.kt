package com.skytoph.taski.core.reminder.worker

import android.app.AlarmManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.gson.Gson
import com.skytoph.taski.core.reminder.HabitUriConverter
import com.skytoph.taski.core.reminder.ReminderItem
import com.skytoph.taski.core.reminder.ReminderScheduler
import java.util.concurrent.TimeUnit

class WorkScheduler(
    private val workManager: WorkManager,
    private val uriConverter: HabitUriConverter,
    private val gson: Gson,
) : ReminderScheduler {

    override fun scheduleRepeating(items: List<ReminderItem>) {
        for (item in items) {
            val currentTime = System.currentTimeMillis()
            val targetTime = item.timeMillis
            val delay = targetTime - currentTime
            workManager.enqueueUniquePeriodicWork(
                item.uri + "name",
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicRequest(item, delay)
            )
        }
    }

    override fun schedule(items: List<ReminderItem>) {
        for (item in items) {
            val currentTime = System.currentTimeMillis()
            val targetTime = item.timeMillis
            val delay = targetTime - currentTime
            workManager.enqueue(request(item, delay))
        }
    }

    override fun reschedule(item: ReminderItem) {
        if (item.interval.reschedule)
            schedule(items = listOf(item.copy(timeMillis = item.interval.next(item.timeMillis, item.day))))
    }

    private fun request(item: ReminderItem, delay: Long): WorkRequest {
        val builder = OneTimeWorkRequest.Builder(ReminderWorker::class.java)
        return request(item, delay, builder)
    }

    private fun periodicRequest(item: ReminderItem, delay: Long): PeriodicWorkRequest {
        val builder = PeriodicWorkRequestBuilder<ReminderWorker>(item.interval.interval.toLong(), TimeUnit.DAYS)
        return request(item, delay, builder)
    }

    private fun <W : WorkRequest, B : WorkRequest.Builder<B, W>>
            request(item: ReminderItem, delay: Long, builder: WorkRequest.Builder<B, W>): W {
        val data = Data.Builder().putString(ReminderItem.KEY_ITEM, gson.toJson(item)).build()
        val request = builder
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(item.uri)
            .build()
        return request
    }

    override fun areNotificationsAllowed(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).canScheduleExactAlarms()

    override fun cancel(uri: Uri) {
        workManager.cancelAllWorkByTag(uri.toString())
    }

    override fun cancel(id: Long, times: Int) =
        (0 until times).forEach { index ->
            cancel(uriConverter.uri(id, index))
        }
}