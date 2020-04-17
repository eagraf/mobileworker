package com.github.eagraf.mobileworker

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import android.widget.Toast

class PluginJobService : JobService() {

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
        return false
    }

}