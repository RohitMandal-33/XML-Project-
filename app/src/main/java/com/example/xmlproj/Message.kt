// File: Message.kt
package com.example.xmlproj

data class Message(
    val id: Int,
    val sender: String,
    val subject: String,
    val preview: String,
    val timestamp: String,
    var isRead: Boolean = false
)