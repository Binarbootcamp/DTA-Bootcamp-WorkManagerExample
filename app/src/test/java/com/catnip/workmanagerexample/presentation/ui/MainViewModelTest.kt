package com.catnip.workmanagerexample.presentation.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catnip.workmanagerexample.data.local.EventDataSource
import com.catnip.workmanagerexample.data.model.Event
import com.catnip.workmanagerexample.tools.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.*

/**
 * Written with love by Muhammad Hermas Yuda Pamungkas
 * Github : https://github.com/hermasyp
 */
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @MockK
    private lateinit var eventDataSource: EventDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = MainViewModel(eventDataSource)
    }

    @Test
    fun testEmptyEventList() {
        val result = listOf<Event>()
        every { eventDataSource.getStoredEvent() } returns result
        viewModel.refreshEvent()
        Assert.assertEquals(viewModel.events.getOrAwaitValue(),result)
    }

    @Test
    fun testEventListReceived() {
        val result = mutableListOf<Event>().apply {
            add(mockk( relaxed = true))
            add(mockk( relaxed = true))
            add(mockk( relaxed = true))
        }
        every { eventDataSource.getStoredEvent() } returns result
        viewModel.refreshEvent()
        Assert.assertEquals(viewModel.events.getOrAwaitValue(),result)
        Assert.assertEquals(viewModel.events.getOrAwaitValue().size,result.size)
    }

    @After
    fun after() {
        unmockkAll()
    }
}