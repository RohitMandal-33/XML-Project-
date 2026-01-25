package com.example.xmlproj

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        val btnSend = findViewById<Button>(R.id.button3)
        val editText = findViewById<EditText>(R.id.editTextText)

        btnSend.setOnClickListener {
            val textToSend = editText.text.toString()
            val resultIntent = Intent()
            resultIntent.putExtra("EXTRA_DATA", textToSend)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        val btnAction = findViewById<Button>(R.id.btnAction)
        btnAction.setOnClickListener {
            Thread {
                try {
                    val url = java.net.URL("https://www.google.com")
                    val connection = url.openConnection() as java.net.HttpURLConnection
                    connection.connect()

                    Log.d("INTERNET", "Connection successful")

                } catch (e: Exception) {
                    Log.e("INTERNET", "Connection failed: ${e.message}")
                }
            }.start()
        }
    }
}
