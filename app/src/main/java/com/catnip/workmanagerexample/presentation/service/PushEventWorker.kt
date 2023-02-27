package com.catnip.workmanagerexample.presentation.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
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
                Log.d(TAG, "doWork: Push Data Aborted, Events is empty")
                return Result.success()
            } else {
                val result = analyticsDataSource.pushEvent(events).first()
                if (result.isSuccess) {
                    Log.d(TAG, "doWork: Push Data Success :  ${result.message}")
                    eventDataSource.flushEvent()
                    Result.success()
                } else {
                    Log.d(TAG, "doWork: Push Data Error by response :  ${result.message}")
                    Result.retry()
                }
            }
        } catch (e: HttpException) {
            Log.d(TAG, "doWork: Push Data Error caught an Exception :  ${e.message}")
            Result.retry()
        }
    }

    companion object {
        val TAG = PushEventWorker::class.simpleName
    }
}