package com.github.skytoph.taski.core.alarm

import android.content.Context
import android.net.Uri
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class WorkScheduler(
    private val workManager: WorkManager,
    private val uriConverter: HabitUriConverter,
    private val gson: Gson,
) : ReminderScheduler {

    override fun scheduleRepeating(context: Context, items: List<AlarmItem>) {
        for (item in items) {
            val currentTime = System.currentTimeMillis()
            val targetTime = item.timeMillis
                .let { if (it < currentTime) item.interval.next(it, item.day) else it }
            val delay = targetTime - currentTime
            workManager.enqueueUniquePeriodicWork(
                item.uri + "name",
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicRequest(item, delay)
            )
        }
    }

    override fun schedule(context: Context, items: List<AlarmItem>) {
        for (item in items) {
            val currentTime = System.currentTimeMillis()
            val targetTime = item.timeMillis
                .let { if (it < currentTime) item.interval.next(it, item.day) else it }
            val delay = targetTime - currentTime
            workManager.enqueue(request(item, delay))
        }
    }

    private fun request(item: AlarmItem, delay: Long): WorkRequest {
        val builder = OneTimeWorkRequest.Builder(ReminderWorker::class.java)
        return request(item, delay, builder)
    }

    private fun periodicRequest(item: AlarmItem, delay: Long): PeriodicWorkRequest {
        val builder = PeriodicWorkRequestBuilder<ReminderWorker>(item.interval.interval.toLong(), TimeUnit.DAYS)
        return request(item, delay, builder)
    }

    private fun <W : WorkRequest, B : WorkRequest.Builder<B, W>>
            request(item: AlarmItem, delay: Long, builder: WorkRequest.Builder<B, W>): W {
        val data = Data.Builder().putString(ReminderWorker.KEY_ITEM, gson.toJson(item)).build()
        val request = builder
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(item.uri)
            .build()
        return request
    }

    override fun cancel(context: Context, uri: Uri) {
        workManager.cancelAllWorkByTag(uri.toString())
    }

    override fun cancel(context: Context, id: Long, times: Int) =
        (0 until times).forEach { index ->
            cancel(context, uriConverter.uri(id, index))
        }
}