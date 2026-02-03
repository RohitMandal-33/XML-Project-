package com.example.xmlproj

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlproj.R
import com.example.xmlproj.Message

class MessageAdapter(
    private val messages: MutableList<Message>,
    private val onItemClick: (Message) -> Unit
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textSender: TextView = itemView.findViewById(R.id.textSender)
        val textSubject: TextView = itemView.findViewById(R.id.textSubject)
        val textPreview: TextView = itemView.findViewById(R.id.textPreview)
        val textTime: TextView = itemView.findViewById(R.id.textTime)

        fun bind(message: Message) {
            textSender.text = message.sender
            textSubject.text = message.subject
            textPreview.text = message.preview
            textTime.text = message.timestamp

            // Change appearance based on read status
            if (message.isRead) {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, android.R.color.white)
                )
                textSender.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.darker_gray)
                )
                textSubject.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.darker_gray)
                )
            } else {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.unread_background)
                )
                textSender.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.black)
                )
                textSubject.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.black)
                )
            }

            itemView.setOnClickListener {
                message.isRead = true
                notifyItemChanged(adapterPosition)
                onItemClick(message)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    // Method to get unread messages
    fun getUnreadCount(): Int {
        return messages.count { !it.isRead }
    }

    // Method to mark all as read
    fun markAllAsRead() {
        messages.forEach { it.isRead = true }
        notifyDataSetChanged()
    }
}