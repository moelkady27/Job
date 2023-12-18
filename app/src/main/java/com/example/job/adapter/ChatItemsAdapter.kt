package com.example.job.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.job.AppReference
import com.example.job.R
import com.example.job.activities.ChattingActivity
import com.example.job.model.Chat
import com.example.job.model.User
import kotlinx.android.synthetic.main.each_row_chat.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatItemsAdapter (
    private val chatList: List<Chat>

) : RecyclerView.Adapter<ChatItemsAdapter.MyViewHolder>(){


    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.each_row_chat,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chat = chatList[position]

        val currentUserId = AppReference.getUserId(holder.itemView.context as Activity?)
        val firstUser = chat.users.firstOrNull()
        val secondUser = chat.users.lastOrNull()
        val otherUser: User?
        if (firstUser?._id == currentUserId) {
            otherUser = secondUser
        } else {
            otherUser = firstUser
        }

        val chatId = chat._id
        val receiverId = otherUser!!._id

        holder.itemView.textNameChat.text = otherUser?.username

        val imageUrl = otherUser?.imageUrl
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.itemView.imageView9)
        }

        val createdAt = otherUser?.createdAt
        val customTime = convertTimestampToCustomFormat(createdAt)
        holder.itemView.textDateChat.text = customTime

        holder.itemView.textViewLatestMessage.text = chat.latestMessage.content

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ChattingActivity::class.java)
            Log.e("chatId", chatId)
            Log.e("receiverId", receiverId)
            intent.putExtra("chatId", chatId)
            intent.putExtra("otherUserId", receiverId)
            intent.putExtra("receiverUsername", otherUser.username)
            intent.putExtra("receiverImage", otherUser.imageUrl)
            context.startActivity(intent)
        }

    }

    private fun convertTimestampToCustomFormat(timestamp: String?): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm a", Locale.getDefault())

        try {
            val parsedDate = inputFormat.parse(timestamp ?: "")
            return outputFormat.format(parsedDate ?: Date())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

}