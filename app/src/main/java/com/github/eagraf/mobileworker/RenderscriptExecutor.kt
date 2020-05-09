package com.github.eagraf.mobileworker

import android.content.Context
import android.os.AsyncTask
import androidx.renderscript.Allocation
import androidx.renderscript.Element
import androidx.renderscript.RenderScript
import androidx.renderscript.Type
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class RenderscriptExecutor(context: Context) : Executor() {

    private var context = context

    // TODO this pattern aint good
    var script: ScriptC_gol? = null
    var inBuffer: Allocation? = null
    var extraBuffer: Allocation? = null
    var outBuffer: Allocation? = null

    override fun executeGameOfLife(intent: WorkIntent): JSONObject {
        Log.d("RenderscriptExecutor", "Executing")
        val rs = RenderScript.create(context)

        // Allocate buffers
        //val inBuffer = Allocation.createFromBitmap(rs, bitmapIn)
        //val outBuffer = Allocation.createFromBitmap(rs, bitmapOut)

        // Convert to byte array
        val size = intent.input.getInt("size")
        val jsonArray = intent.input.getJSONArray("board")
        Log.d("RenderScriptExecutor", jsonArray.toString())
        val bytes = ByteArray(size*size) {
            i ->  (jsonArray.get(i) as Int).toByte()
        }

        // Allocate input buffer
        inBuffer = Allocation.createTyped(rs, Type.createX(rs, Element.U8(rs), size*size))
        extraBuffer = Allocation.createTyped(rs, Type.createX(rs, Element.U8(rs), size*size))
        // Copy data into buffer
        inBuffer!!.copyFrom(bytes)
        extraBuffer!!.copyFrom(bytes)

        // Initialize outBuffer as well
        val outBytes = ByteArray(size*size) { _ -> 0 }
        outBuffer = Allocation.createTyped(rs, Type.createX(rs, Element.U8(rs), size*size))
        outBuffer!!.copyFrom(outBytes)

        script = ScriptC_gol(rs)

        // Execute task
        val task = RenderScriptTaskGOL()
        task.execute(size)


        return JSONObject()
    }

    // TODO switch to Kotlin coroutines
    inner class RenderScriptTaskGOL : AsyncTask<Int, Void, Integer>() {
        override fun doInBackground(vararg size: Int?): Integer {
            Log.d("RenderScriptExecutor", "Actually executing")
            val start = System.currentTimeMillis()
            script!!.set_board(extraBuffer)
            script!!.set_size(size[0]!!)
            script!!.forEach_gol(inBuffer, outBuffer)


            val bytes = ByteArray(size[0]!! * size[0]!!)
            outBuffer!!.copyTo(bytes)

            val end = System.currentTimeMillis()
            Log.d("RenderScriptExecutor", start.toString() + " " + end.toString())

            Log.d("RenderScriptExecutor", JSONArray(bytes).toString())

            return Integer(0)
        }

    }

}




