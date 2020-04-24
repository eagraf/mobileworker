package com.github.eagraf.mobileworker

import android.os.Build
import android.util.Log
import okhttp3.*
import okio.ByteString
import org.json.JSONObject


class Message(json: String) : JSONObject(json) {
    val messageType: String? = this.getString("MessageType")
    val payload = this.getJSONObject("Payload")
}

class WorkIntent(json: String) : JSONObject(json) {
    val intentType: String? = this.getString("IntentType")
    val taskType: String? = this.getString("TaskType")
    val taskUUID: String? = this.getString("TaskUUID")
    val input = this.getJSONObject("Input")
}

class ConnectionManager(executor: Executor) {
    private var client: OkHttpClient = OkHttpClient()
    var webSocket: WebSocket?
    var connected: Boolean

    // TODO: Some sort of pub sub system for different listeners
    val executor = executor

    init {
        webSocket = null
        connected = false
        Log.d("ConnectionManager", "Initializing")
    }

    fun connect() {
        Log.d("ConnectionManager", "Connecting")
        val request = Request.Builder().url("http://ec2-54-196-13-197.compute-1.amazonaws.com:2216/workers/register/?workertype=" + Build.MODEL).build()
        val listener = WorkerListener(this)

        webSocket = client.newWebSocket(request, listener)

        // TODO evaluate best policy for closing client
        //client.dispatcher().executorService().shutdown()
    }

    fun disconnect() {
        Log.d("ConnectionManager", "Disconnecting")
        if (connected) {
            webSocket!!.close(1000, "User forced close")
            connected = false
        }
    }
}

class WorkerListener(cm: ConnectionManager): WebSocketListener() {

    private var connectionManager: ConnectionManager = cm

    override fun onOpen(webSocket: WebSocket, response: Response) {
        connectionManager.connected = true
        //webSocket.send("hello")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("ConnectionManager","Receiving text: " + text)
        val message = Message(text)
        Log.d("ConnectionManager", message.messageType)
        when (message.messageType) {
            "Intent" -> handleWorkIntent(message.payload)
            else -> Log.d("ConnectionManager", "elso")
        }

    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("ConnectionManager", "Receiving bytes: " + bytes.hex())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        Log.d("ConnectionManager","Closing: " + code + " / " + reason)
        connectionManager.connected = false
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("ConnectionManager", "Error: " + t.message)
        t.printStackTrace()
        connectionManager.connected = false
    }

    fun handleWorkIntent(intent: JSONObject) {
        // Convert into WorkIntent class
        val workIntent = WorkIntent(intent.toString())
        Log.d("ConnectionManager", workIntent.taskType)
        this.connectionManager.executor.onWorkIntentReceived(workIntent, connectionManager)
    }
}