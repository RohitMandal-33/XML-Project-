
package com.example.xmlproj

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.xmlproj.database.AppDatabase
import com.example.xmlproj.database.User
import com.example.xmlproj.repository.UserRepository
import kotlinx.coroutines.launch

class DatabaseTestActivity : AppCompatActivity() {

    private lateinit var repository: UserRepository
    private lateinit var textResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_test)

        val database = AppDatabase.getDatabase(this)
        repository = UserRepository(database.userDao())

        textResult = findViewById(R.id.textResult)
        val btnInsert = findViewById<Button>(R.id.btnInsert)
        val btnViewAll = findViewById<Button>(R.id.btnViewAll)
        val btnClear = findViewById<Button>(R.id.btnClear)

        // Insert sample users
        btnInsert.setOnClickListener {
            insertSampleUsers()
        }

        // View all users
        btnViewAll.setOnClickListener {
            viewAllUsers()
        }

        // Clear database
        btnClear.setOnClickListener {
            clearDatabase()
        }
    }

    private fun insertSampleUsers() {
        lifecycleScope.launch {
            try {
                val users = listOf(
                    User(email = "john@example.com", password = "password123", name = "John Doe"),
                    User(email = "jane@example.com", password = "pass456", name = "Jane Smith"),
                    User(email = "bob@example.com", password = "secret789", name = "Bob Johnson")
                )

                users.forEach { user ->
                    repository.insert(user)
                }

                Toast.makeText(this@DatabaseTestActivity,
                    "Users inserted!", Toast.LENGTH_SHORT).show()
                viewAllUsers()
            } catch (e: Exception) {
                Toast.makeText(this@DatabaseTestActivity,
                    "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun viewAllUsers() {
        lifecycleScope.launch {
            try {
                repository.allUsers.collect { users ->
                    val result = StringBuilder()
                    result.append("Total Users: ${users.size}\n\n")
                    users.forEach { user ->
                        result.append("ID: ${user.id}\n")
                        result.append("Name: ${user.name}\n")
                        result.append("Email: ${user.email}\n")
                        result.append("---\n")
                    }
                    textResult.text = result.toString()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DatabaseTestActivity,
                    "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearDatabase() {
        lifecycleScope.launch {
            try {
                repository.deleteAll()
                Toast.makeText(this@DatabaseTestActivity,
                    "Database cleared!", Toast.LENGTH_SHORT).show()
                textResult.text = "Database is empty"
            } catch (e: Exception) {
                Toast.makeText(this@DatabaseTestActivity,
                    "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}