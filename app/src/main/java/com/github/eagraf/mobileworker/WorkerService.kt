package com.github.eagraf.mobileworker

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import android.util.Log

// TODO determine when to stop service/connection when device is no longer idle
class WorkerService : IntentService(WorkerService::class.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        Log.d("WorkerService", "I have risen")

        // Start connection with synchronizer server
        // Setup websocket connection
        val cm = ConnectionManager()
        cm.connect()
    }
}