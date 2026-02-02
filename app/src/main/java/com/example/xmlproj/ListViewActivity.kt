package com.example.xmlproj

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.xmlproj.StudentAdapter
import com.example.xmlproj.Student

class ListViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        val listView = findViewById<ListView>(R.id.listView)

        // Sample data
        val students = listOf(
            Student(1, "Alice Johnson", "A", "alice@school.com"),
            Student(2, "Bob Smith", "B+", "bob@school.com"),
            Student(3, "Charlie Brown", "A-", "charlie@school.com"),
            Student(4, "Diana Prince", "A+", "diana@school.com"),
            Student(5, "Eve Wilson", "B", "eve@school.com")
        )

        val adapter = StudentAdapter(this, students)
        listView.adapter = adapter

        // Item click listener
        listView.setOnItemClickListener { _, _, position, _ ->
            val student = students[position]
            Toast.makeText(
                this,
                "Clicked: ${student.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}