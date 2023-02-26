package com.catnip.workmanagerexample.presentation.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.catnip.workmanagerexample.data.local.EventDataSource
import com.catnip.workmanagerexample.data.network.AnalyticsDataSource
import com.catnip.workmanagerexample.data.network.HttpException
import com.catnip.workmanagerexample.provider.InjectionProvider
import kotlinx.coroutines.flow.first

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class PushEventWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val eventDataSource: EventDataSource by lazy {
        InjectionProvider.provideEventDataSource()
    }

    private val analyticsDataSource: AnalyticsDataSource by lazy {
        InjectionProvider.provideAnalyticsDataSource()
    }

    override suspend fun doWork(): Result {
        val events = eventDataSource.getStoredEvent()
        return try {
            if (events.isEmpty()) {
                return Result.success()
            } else {
                val result = analyticsDataSource.pushEvent(events).first()
                if (result.isSuccess) {
                    eventDataSource.flushEvent()
                    Result.success(workDataOf(Pair(DATA_TOTAL_EVENT, events.size)))
                } else Result.retry()
            }
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val DATA_TOTAL_EVENT = "DATA_TOTAL_EVENT"
    }
}