package com.catnip.workmanagerexample.data.network

import android.util.Log
import com.catnip.workmanagerexample.data.model.Event
import com.catnip.workmanagerexample.data.model.PushEventResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface AnalyticsDataSource {
    fun pushEvent(events: List<Event>): Flow<PushEventResponse>
}

class MockAnalyticsDataSource : AnalyticsDataSource {

    companion object{
        val TAG = MockAnalyticsDataSource::class.simpleName
    }

    override fun pushEvent(events: List<Event>): Flow<PushEventResponse> = flow {
        delay(2000)
        //mock server error
        if (Random.nextBoolean()) {
            //mock data error
            if(Random.nextBoolean()){
                Log.d(TAG, "pushEvent: Mock Success Response")
                emit(
                    PushEventResponse(
                        isSuccess = true,
                        code = 200,
                        message = "${events.size} Event been pushed successfully"
                    )
                )
            }else{
                Log.d(TAG, "pushEvent: Mock Failed Response")
                emit(
                    PushEventResponse(
                        isSuccess = false,
                        code = 400,
                        message = "Events Failed to push"
                    )
                )
            }
        } else {
            Log.d(TAG, "pushEvent: Mock Http Exception")
            throw HttpException(500, "Internal Server Error")
        }
    }

}