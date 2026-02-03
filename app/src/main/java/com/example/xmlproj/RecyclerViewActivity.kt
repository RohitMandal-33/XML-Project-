package com.example.xmlproj

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlproj.MessageAdapter
import com.example.xmlproj.Message

class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var adapter: MessageAdapter
    private lateinit var textUnreadCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        textUnreadCount = findViewById(R.id.textUnreadCount)
        val btnMarkAllRead = findViewById<Button>(R.id.btnMarkAllRead)

        // Sample data
        val messages = mutableListOf(
            Message(1, "John Doe", "Meeting Tomorrow",
                "Don't forget about tomorrow's meeting at 10 AM", "10:30 AM", false),
            Message(2, "Jane Smith", "Project Update",
                "The project is progressing well. Please review", "9:15 AM", false),
            Message(3, "Bob Johnson", "Lunch Plans",
                "Are you free for lunch today?", "Yesterday", true),
            Message(4, "Alice Williams", "Weekend Trip",
                "Planning a trip this weekend. Interested?", "Yesterday", false),
            Message(5, "Charlie Brown", "Code Review",
                "Can you review my pull request?", "2 days ago", false)
        )

        adapter = MessageAdapter(messages) { message ->
            Toast.makeText(
                this,
                "Opened: ${message.subject}",
                Toast.LENGTH_SHORT
            ).show()
            updateUnreadCount()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Mark all as read button
        btnMarkAllRead.setOnClickListener {
            adapter.markAllAsRead()
            updateUnreadCount()
        }

        updateUnreadCount()
    }

    private fun updateUnreadCount() {
        val count = adapter.getUnreadCount()
        textUnreadCount.text = "Unread: $count"
    }
}