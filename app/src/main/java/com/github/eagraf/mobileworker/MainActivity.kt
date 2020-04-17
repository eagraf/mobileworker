package com.github.eagraf.mobileworker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ToggleButton

class MainActivity : AppCompatActivity() {

    val NOTIFICATION_CHANNEL_ID = "mobile_worker_notification_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        // Setup UI
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

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
