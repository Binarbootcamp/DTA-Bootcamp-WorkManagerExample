package com.catnip.workmanagerexample.presentation.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.catnip.workmanagerexample.data.model.Event
import com.catnip.workmanagerexample.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }

    companion object {
        const val WORKER_EVENTS = "WORKER_EVENTS"
        val TAG = MainActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startWorker()
        setupUI()
    }

    private fun setupUI() {
        setupButton()
        setupText()
        viewModel.refreshEvent()
    }

    private fun setupButton() {
        binding.btnCreateEvent.setOnClickListener {
            viewModel.storeEvent(generateRandomEvent())
        }
    }

    private fun setupText() {
        viewModel.events.observe(this) {
            binding.tvDebug.text = "${it.size} Events stored"
        }
    }

    private fun generateRandomEvent() = Event(
        eventName = "button_pushed",
        hashMapOf(
            "source" to "Main Page",
            "uuid" to UUID.randomUUID().toString()
        )
    )

    private fun startWorker() {
       //todo : start the worker here
    }
}

