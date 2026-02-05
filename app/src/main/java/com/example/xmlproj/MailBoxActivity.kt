package com.example.xmlproj

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlproj.MessageAdapter
import com.example.xmlproj.Message

class MailBoxActivity : AppCompatActivity() {

    private lateinit var adapter: MessageAdapter
    private lateinit var textUnreadBadge: TextView
    private lateinit var textTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_box)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMail)
        textUnreadBadge = findViewById(R.id.textUnreadBadge)
        textTitle = findViewById(R.id.textTitle)
        val btnMarkAllRead = findViewById<Button>(R.id.btnMarkAllRead)
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)

        // Sample messages
        val messages = createSampleMessages()

        adapter = MessageAdapter(messages) { message ->
            // Handle message click
            updateUnreadBadge()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnMarkAllRead.setOnClickListener {
            adapter.markAllAsRead()
            updateUnreadBadge()
        }

        btnRefresh.setOnClickListener {
            // Simulate new messages
            addNewMessage()
            updateUnreadBadge()
        }

        updateUnreadBadge()
    }

    private fun createSampleMessages(): MutableList<Message> {
        return mutableListOf(
            Message(1, "John Doe", "Meeting Reminder",
                "Don't forget about tomorrow's meeting", "10:30 AM", false),
            Message(2, "Jane Smith", "Project Update",
                "The project is going well", "9:15 AM", false),
            Message(3, "Support Team", "Welcome!",
                "Thank you for joining us", "Yesterday", true),
            Message(4, "Alice Williams", "Question",
                "Can you help me with this?", "Yesterday", false),
            Message(5, "Bob Johnson", "Lunch Tomorrow",
                "Are you free for lunch?", "2 days ago", false)
        )
    }

    private fun addNewMessage() {
        // This would normally come from a server
        // For demo, we're just adding to the list
        adapter.notifyDataSetChanged()
    }

    private fun updateUnreadBadge() {
        val unreadCount = adapter.getUnreadCount()

        if (unreadCount > 0) {
            textUnreadBadge.text = unreadCount.toString()
            textUnreadBadge.visibility = android.view.View.VISIBLE
            textTitle.text = "Inbox ($unreadCount unread)"
        } else {
            textUnreadBadge.visibility = android.view.View.GONE
            textTitle.text = "Inbox"
        }
    }
}