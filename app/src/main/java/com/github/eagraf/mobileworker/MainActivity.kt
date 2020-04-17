package com.github.eagraf.mobileworker

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ToggleButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Setup UI
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Schedule plugin job
        val jobInfo = JobInfo.Builder(2216, ComponentName(this, PluginJobService::class.java))
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            .setRequiresCharging(true)
            //.setRequiresDeviceIdle(true)
            .build()

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)

        // Setup websocket connection
        val cm = ConnectionManager()
        val connectionToggle: ToggleButton = findViewById(R.id.connectionToggle)
        connectionToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Attempt to connect to synchronizer
                Log.d("MainActivity", "Connect")
                cm.connect()

            } else {
                // Disconnect from synchronizer
                Log.d("MainActivity", "Disconnect")
                cm.disconnect()
            }
        }

    }
}
