package com.example.xmlproj

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.xmlproj.database.AppDatabase
import com.example.xmlproj.database.User
import com.example.xmlproj.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var repository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val database = AppDatabase.getDatabase(this)
        repository = UserRepository(database.userDao())

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val textLogin = findViewById<TextView>(R.id.textLogin)

        btnRegister.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            if (validateInput(name, email, password, confirmPassword)) {
                performRegistration(name, email, password)
            }
        }

        textLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show()
            return false
        }

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

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun performRegistration(name: String, email: String, password: String) {
        lifecycleScope.launch {
            try {
                // Check if email already exists
                val existingUser = repository.getUserByEmail(email)
                if (existingUser != null) {
                    Toast.makeText(this@RegisterActivity,
                        "Email already registered", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Create new user
                val newUser = User(
                    email = email,
                    password = password,
                    name = name
                )

                repository.insert(newUser)

                Toast.makeText(this@RegisterActivity,
                    "Registration successful! Please login", Toast.LENGTH_SHORT).show()

                finish()
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity,
                    "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}