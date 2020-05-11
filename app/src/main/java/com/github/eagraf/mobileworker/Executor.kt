package com.github.eagraf.mobileworker

import android.os.Build
import android.util.Log
import org.json.JSONObject

abstract class Executor() {
    fun onWorkIntentReceived(intent: WorkIntent, connectionManager: ConnectionManager) {

        val start = System.currentTimeMillis()

        // Choose what to execute
        lateinit var message: JSONObject
        when(intent.taskType) {
            "Hello" -> Log.d("Executor", "Hello Task")
            "GOL" -> message = executeGameOfLife(intent)
        }

        if (message != null) {
            val end = System.currentTimeMillis()

            Log.d("Executor", Build.MODEL)
            Log.d("Executor", Build.MANUFACTURER)

            // Build message
            message.put("MessageType", "WorkResponse")
            message.put("start", start)
            message.put("end", end)
            message.put("device", Build.MODEL)
            Log.d("Time:", start.toString() + ", " + end.toString())

            // Send message
            connectionManager.send(message)
        }
    }

    // Implementions for game of life
    abstract fun executeGameOfLife(intent: WorkIntent) : JSONObject
}
