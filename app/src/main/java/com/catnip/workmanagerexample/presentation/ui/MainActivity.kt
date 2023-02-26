package com.catnip.workmanagerexample.presentation.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.catnip.workmanagerexample.data.model.Event
import com.catnip.workmanagerexample.databinding.ActivityMainBinding
import com.catnip.workmanagerexample.presentation.service.PushEventWorker
import java.util.*
import java.util.concurrent.TimeUnit

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
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Minimum periodic time is 15 minute and retry time is 10 Second
        val worker = PeriodicWorkRequestBuilder<PushEventWorker>(15, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .setInitialDelay(5,TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()
        val workManager = WorkManager.getInstance(this.applicationContext)
        workManager.enqueueUniquePeriodicWork(
            WORKER_EVENTS,
            ExistingPeriodicWorkPolicy.KEEP,
            worker
        )
        workManager.getWorkInfoByIdLiveData(worker.id)
            .observe(this) {
                Log.d(TAG, "startWorker: ${it.state}")
                when (it.state) {
                    WorkInfo.State.ENQUEUED -> {
                        val totalPushedEvent = it.outputData.getInt(PushEventWorker.DATA_TOTAL_EVENT,0)
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Worker Enqueue push data to Server : $totalPushedEvent Events", Toast.LENGTH_SHORT).show()
                        }
                        viewModel.refreshEvent()
                    }
                    WorkInfo.State.RUNNING -> {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Worker Running", Toast.LENGTH_SHORT).show()
                        }
                        viewModel.refreshEvent()
                    }
                    else -> {
                        viewModel.refreshEvent()
                    }
                }
            }
    }
}

