package com.example.xmlproj

import android.app.Service
import android.content.Intent
import android.os.*
import android.widget.Toast

class MyIntentService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    // Handler that receives messages from the background thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: android.os.Message) {
            // Simulate some background work
            try {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        applicationContext,
                        "Service is working on task ${msg.arg1}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Simulate work
                Thread.sleep(5000)

                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        applicationContext,
                        "Task ${msg.arg1} completed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }

            // Stop the service using startId
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        super.onCreate()
        // Create a background thread
        HandlerThread("ServiceWorkThread", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show()

        // Send a message to the background handler
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        // If service gets killed, restart it
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // We don't provide binding
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show()
        serviceLooper?.quit()
    }
}
