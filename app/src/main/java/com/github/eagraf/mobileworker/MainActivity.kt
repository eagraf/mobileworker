package com.github.eagraf.mobileworker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ToggleButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cm = ConnectionManager()

        val connectionToggle: ToggleButton = findViewById(R.id.connectionToggle)
        connectionToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Attempt to connect to synchronizer
                Log.d("MainActivity", "Connect")
                cm.connect()

            } else {
                // Disconnect from synchronizer
                Log.d("MainActivity", "Disconnect")
                cm.disconnect()
            }
        }

    }
}
