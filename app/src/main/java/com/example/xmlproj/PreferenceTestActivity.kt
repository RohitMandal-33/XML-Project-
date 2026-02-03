package com.example.xmlproj

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PreferenceTestActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_test)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scroll_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        preferenceManager = PreferenceManager(this)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnLoad = findViewById<Button>(R.id.btnLoad)
        val textViewResult = findViewById<TextView>(R.id.textViewResult)

        // Save data
        btnSave.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()

            preferenceManager.saveUserName(name)
            preferenceManager.saveUserEmail(email)
            preferenceManager.setLoggedIn(true)

            textViewResult.text = "Data saved successfully!"
        }

        // Load data
        btnLoad.setOnClickListener {
            val name = preferenceManager.getUserName()
            val email = preferenceManager.getUserEmail()
            val isLoggedIn = preferenceManager.isLoggedIn()

            textViewResult.text = "Name: $name\nEmail: $email\nLogged In: $isLoggedIn"
        }
    }
}