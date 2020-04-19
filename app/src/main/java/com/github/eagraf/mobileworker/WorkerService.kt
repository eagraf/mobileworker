package com.github.eagraf.mobileworker

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

// TODO determine when to stop service/connection when device is no longer idle
class WorkerService : IntentService(WorkerService::class.simpleName) {
    val NOTIFICATION_CHANNEL_ID = "mobile_worker_notification_channel"

    override fun onHandleIntent(intent: Intent?) {
        Log.d("WorkerService", "I have risen")
        Toast.makeText(this, "Alarm", Toast.LENGTH_LONG).show()

        var notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Mobile Worker is doing work")
            .setContentText("Woohoo")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, notificationBuilder.build())
        }


        // Start connection with synchronizer server
        // Setup websocket connection
        val executor = Executor()
        val cm = ConnectionManager(executor)
        cm.connect()
    }
}