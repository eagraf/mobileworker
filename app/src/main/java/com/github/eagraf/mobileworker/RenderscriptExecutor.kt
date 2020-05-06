package com.github.eagraf.mobileworker

import android.util.Log
import org.json.JSONObject

class RenderscriptExecutor : Executor() {

    override fun executeGameOfLife(intent: WorkIntent): JSONObject {
        Log.d("RenderscriptExecutor", "Executing")
        return JSONObject()
    }
}
