package com.catnip.workmanagerexample.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.catnip.workmanagerexample.data.local.EventDataSource
import com.catnip.workmanagerexample.data.model.Event
import com.catnip.workmanagerexample.provider.InjectionProvider

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class MainViewModel(private val eventDataSource: EventDataSource) : ViewModel() {

    val events = MutableLiveData<List<Event>>()

    fun storeEvent(event: Event) {
        eventDataSource.pushEvent(event)
        refreshEvent()
    }

    fun refreshEvent() {
        events.postValue(eventDataSource.getStoredEvent())
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return MainViewModel(InjectionProvider.provideEventDataSource()) as T
            }
        }
    }
}