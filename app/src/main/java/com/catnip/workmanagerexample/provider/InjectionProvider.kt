package com.catnip.workmanagerexample.provider

import com.catnip.workmanagerexample.data.local.EventDataSource
import com.catnip.workmanagerexample.data.local.MockEventDataSource
import com.catnip.workmanagerexample.data.network.AnalyticsDataSource
import com.catnip.workmanagerexample.data.network.MockAnalyticsDataSource

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
object InjectionProvider {
    private val eventDataSource: EventDataSource = MockEventDataSource()
    private val analyticsDataSource: AnalyticsDataSource = MockAnalyticsDataSource()

    fun provideEventDataSource(): EventDataSource = eventDataSource
    fun provideAnalyticsDataSource(): AnalyticsDataSource = analyticsDataSource
}