package com.example.xmlproj

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.xmlproj.database.AppDatabase
import com.example.xmlproj.repository.UserRepository
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var repository: UserRepository
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize preferences
        preferenceManager = PreferenceManager(this)

        // Check if already logged in
        if (preferenceManager.isLoggedIn()) {
            navigateToHome()
            return
        }

        setContentView(R.layout.activity_login)

        // Initialize database
        val database = AppDatabase.getDatabase(this)
        repository = UserRepository(database.userDao())

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val textRegister = findViewById<TextView>(R.id.textRegister)

        btnLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString()

            if (validateInput(email, password)) {
                performLogin(email, password)
            }
        }

        textRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters",
                Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun performLogin(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val user = repository.login(email, password)

                if (user != null) {
                    // Save login state
                    preferenceManager.setLoggedIn(true)
                    preferenceManager.saveUserEmail(user.email)
                    preferenceManager.saveUserName(user.name)

                    Toast.makeText(this@LoginActivity,
                        "Welcome ${user.name}!", Toast.LENGTH_SHORT).show()

                    navigateToHome()
                } else {
                    Toast.makeText(this@LoginActivity,
                        "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity,
                    "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
