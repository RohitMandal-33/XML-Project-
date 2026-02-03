package com.example.xmlproj

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        val btnList = findViewById<Button>(R.id.buttonlist)
        btnList.setOnClickListener {
            val intent = Intent(this, ListViewActivity::class.java)
            startActivity(intent)
        }
        val btnrecycle = findViewById<Button>(R.id.buttonrecycle)
        btnrecycle.setOnClickListener {
            val intent = Intent(this, RecyclerViewActivity::class.java)
            startActivity(intent)
        }
        val btndatabase = findViewById<Button>(R.id.btndatabase)
        btndatabase.setOnClickListener {
            val intent = Intent(this, DatabaseTestActivity::class.java)
            startActivity(intent)
        }
    }
}
