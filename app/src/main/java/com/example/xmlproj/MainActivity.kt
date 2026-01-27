package com.example.xmlproj

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "example_channel"

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getStringExtra("EXTRA_DATA")
            val textView = findViewById<TextView>(R.id.textView)
            textView.text = data
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            sendNotification()
        } else {
            Log.w("NOTIFICATION", "Permission denied by user")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFECYCLE", "onCreate")
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        // Button to open SecondActivity
        val btnOpenSecond = findViewById<Button>(R.id.btnOpenSecond)
        btnOpenSecond.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            getResult.launch(intent)
        }

        // Button to open Web URL (Implicit Intent)
        val btnOpenWeb = findViewById<Button>(R.id.btnOpenWeb)
        btnOpenWeb.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.google.com")
            startActivity(intent)
        }

        // Notification Button
        val btnShowNotification = findViewById<Button>(R.id.btnShowNotification)
        btnShowNotification.setOnClickListener {
            checkPermissionAndShowNotification()
        }

        // Intent Service Buttons
        val btnStartService = findViewById<Button>(R.id.btnStartService)
        val btnStopService = findViewById<Button>(R.id.btnStopService)

        btnStartService.setOnClickListener {
            val intent = Intent(this, MyIntentService::class.java)
            startService(intent)
        }

        btnStopService.setOnClickListener {
            val intent = Intent(this, MyIntentService::class.java)
            stopService(intent)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Example Channel"
            val descriptionText = "Channel for example notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkPermissionAndShowNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                sendNotification()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            sendNotification()
        }
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Notification Alert")
            .setContentText("This is a test notification from XML Proj!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        try {
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(1, builder.build())
            }
        } catch (e: SecurityException) {
            Log.e("NOTIFICATION", "Security Exception: ${e.message}")
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("LIFECYCLE", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LIFECYCLE", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LIFECYCLE", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LIFECYCLE", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LIFECYCLE", "onDestroy")
    }
}
