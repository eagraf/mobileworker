package com.github.eagraf.mobileworker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

// AlarmReceiver is triggered at scheduled times. It initiates workerService, and schedules an alarm for the next day
class AlarmReceiver : BroadcastReceiver() {

    val NOTIFICATION_CHANNEL_ID = "mobile_worker_notification_channel"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "Alarm Triggered")
        // Notify the user
        /*var notificationBuilder = NotificationCompat.Builder(context!!, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Mobile Worker is doing work")
            .setContentText("Woohoo")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context!!)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, notificationBuilder.build())
        }*/

        val code = intent!!.getIntExtra("code", 0)
        // Set next alarm
        //setRepeatAlarm(context!!, 10000)
        setRepeatAlarm(context!!, 24 * 60 * 60 * 1000, code)

        // Intent to create worker service
        val workerIntent = Intent(context!!, WorkerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context!!.startForegroundService(workerIntent)
        } else {
            context!!.startService(workerIntent)
        }
    }
}