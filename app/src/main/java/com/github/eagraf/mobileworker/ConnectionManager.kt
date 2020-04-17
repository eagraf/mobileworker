package com.github.eagraf.mobileworker

import android.util.Log
import okhttp3.*
import okio.ByteString

class ConnectionManager {
    private var client: OkHttpClient = OkHttpClient()
    private var webSocket: WebSocket?
    var connected: Boolean

    init {
        webSocket = null
        connected = false
        Log.d("ConnectionManager", "Initializing")
    }

    fun connect() {
        Log.d("ConnectionManager", "Connecting")
        val request = Request.Builder().url("http://ec2-54-196-13-197.compute-1.amazonaws.com:2216/workers?workertype=mobileworker").build()
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
        webSocket.send("hello")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("ConnectionManager","Receiving text: " + text)
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
}