package com.catnip.workmanagerexample.data.local

import com.catnip.workmanagerexample.data.model.Event

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface EventDataSource {
    fun getStoredEvent(): List<Event>
    fun pushEvent(event: Event)
    fun flushEvent()
}

class MockEventDataSource : EventDataSource {

    private val events = mutableListOf<Event>()

    override fun getStoredEvent(): List<Event> = events

    override fun pushEvent(event: Event) {
        events.add(event)
    }
    override fun flushEvent() {
        events.clear()
    }
}