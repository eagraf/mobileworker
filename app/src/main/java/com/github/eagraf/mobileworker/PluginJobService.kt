package com.github.eagraf.mobileworker

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

// TODO handle scheduling in boot receiver so that this job will be scheduled every time the device is plugged in
class PluginJobService : JobService() {
    // Make this value truly global
    val NOTIFICATION_CHANNEL_ID = "mobile_worker_notification_channel"

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("PluginJobService", "Job stopped")
        Toast.makeText(this, "Job stopped", Toast.LENGTH_LONG).show()
        // TODO notify synchronizer of disconnect

        // Returning true signifies the system should try to reschedule this job. i.e. make worker available again
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("PluginJobService", "Device Plugged In")
        Toast.makeText(this, "Device Plugged In", Toast.LENGTH_LONG).show()
        // Notify the user
        var notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Mobile Worker is doing work")
            .setContentText("Woohoo")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, notificationBuilder.build())
        }

        // Intent to create worker service
        val workerIntent = Intent(this, WorkerService::class.java)
        startService(workerIntent)

        return true
    }

}