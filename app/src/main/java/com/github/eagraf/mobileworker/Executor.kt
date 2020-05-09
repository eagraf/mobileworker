package com.github.eagraf.mobileworker

import android.os.Build
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

abstract class Executor() {
    fun onWorkIntentReceived(intent: WorkIntent, connectionManager: ConnectionManager) {

        val start = System.currentTimeMillis()

        lateinit var message: JSONObject
        when(intent.taskType) {
            "Hello" -> Log.d("Executor", "Hello Task")
            "GOL" -> message = executeGameOfLife(intent)
        }

        if (message != null) {
            val end = System.currentTimeMillis()

            Log.d("Executor", Build.MODEL)
            Log.d("Executor", Build.MANUFACTURER)
            message.put("MessageType", "WorkResponse")
            message.put("start", start)
            message.put("end", end)
            message.put("device", Build.MODEL)
            Log.d("Time:", start.toString() + ", " + end.toString())
            //Log.d("Message", message.toString())
            // TODO refactor this into connection manager
            connectionManager.webSocket!!.send(message.toString())
            Log.d("Executor", "I'm still alive")
        }
    }

    abstract fun executeGameOfLife(intent: WorkIntent) : JSONObject

    /*fun executeGameOfLife(intent: WorkIntent, connectionManager: ConnectionManager) : JSONObject {
       class GameOfLifeInput(json: String) : JSONObject(json) {
           val size: Int? = this.getInt("size")
           val board = this.getJSONArray("board")
               ?.let { 0.until(it.length()).map { i -> it.getInt(i) }}
       }

        val input = GameOfLifeInput(intent.input.toString())
        //Log.d("Executor", input.size.toString())
        //Log.d("Executor", input.board.toString())

        // Compute next update
        var nextBoard: MutableList<Int> = ArrayList<Int>(Collections.nCopies(input.size!! * input.size!!, 0))
        for ((index, cell) in input.board!!.withIndex()) {

            // Calculate x and y
            val x: Int = index % input.size
            val y: Int = index / input.size

            val y1 = ((input.size + y - 1) % input.size) * input.size
            val y2 = y * input.size
            val y3 = ((input.size + y + 1) % input.size) * input.size

            val x1 = (input.size + x - 1) % input.size
            val x2 = x
            val x3 = (input.size + x + 1) % input.size

            // Calculate number of adjacent alive cells:
            var count = 0
            count += input.board!![y1+x1]
            count += input.board!![y1+x2]
            count += input.board!![y1+x3]
            count += input.board!![y2+x1]
            count += input.board!![y2+x3]
            count += input.board!![y3+x1]
            count += input.board!![y3+x2]
            count += input.board!![y3+x3]

            if (input.board!![y2+x2] == 0) {
                if (count == 3) {
                    nextBoard[y2+x2] = 1
                } else {
                    nextBoard[y2+x2] = 0
                }
            } else {
                if (count < 2) {
                    nextBoard[y2+x2] = 0
                } else if (count > 3) {
                    nextBoard[y2 + x2] = 0
                } else {
                    nextBoard[y2+x2] = 1
                }
            }
        }
        //Log.d("Executor", nextBoard.toString())

        // Construct response messsage
        val message = JSONObject()
        message.put("Output", JSONArray(nextBoard))

        return message
    }*/
}
