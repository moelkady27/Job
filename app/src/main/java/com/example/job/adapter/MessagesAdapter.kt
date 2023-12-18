package com.example.job.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.job.AppReference
import com.example.job.R
import com.example.job.model.DeleteMessageResponse
import com.example.job.model.Message
import com.example.job.service.RetrofitClient
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MessagesAdapter(
    private var messages: List<Message>,
    private val userId: String,
    private val context: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2
    }

    inner class SentMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tvMessage)
        val tvMessageDate: TextView = view.findViewById(R.id.tvMessageDate)
        val tvUserImage: CircleImageView = view.findViewById(R.id.tvUserImage)
    }

    inner class ReceivedMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tvMessage)
        val tvMessageDate: TextView = view.findViewById(R.id.tvMessageDate)
        val tvUserImage: CircleImageView = view.findViewById(R.id.tvUserImage)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_SENT -> {
                val view = layoutInflater.inflate(R.layout.chat_right, parent, false)
                SentMessageViewHolder(view)
            }
            VIEW_TYPE_RECEIVED -> {
                val view = layoutInflater.inflate(R.layout.chat_left, parent, false)
                ReceivedMessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.sender._id == userId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        when (holder.itemViewType) {
            VIEW_TYPE_SENT -> {
                val sentHolder = holder as SentMessageViewHolder
                sentHolder.tvMessage.text = message.content
                sentHolder.tvMessageDate.text = convertTimestampToCustomFormat(message.updatedAt)
                Glide.with(sentHolder.itemView.context)
                    .load(message.sender.imageUrl)
                    .into(sentHolder.tvUserImage)

                sentHolder.itemView.setOnLongClickListener {
                    showDeleteMessageDialog(message)
                    true
                }
            }
            VIEW_TYPE_RECEIVED -> {
                val receivedHolder = holder as ReceivedMessageViewHolder
                receivedHolder.tvMessage.text = message.content
                receivedHolder.tvMessageDate.text = convertTimestampToCustomFormat(message.updatedAt)
                Glide.with(receivedHolder.itemView.context)
                    .load(message.sender.imageUrl)
                    .into(receivedHolder.tvUserImage)

                receivedHolder.itemView.setOnLongClickListener {
                    showDeleteMessageDialog(message)
                    true
                }
            }
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

    fun addMessages(newMessages: List<Message>) {
        val currentList = mutableListOf<Message>()
        currentList.addAll(messages)
        currentList.addAll(newMessages)
        messages = currentList
        notifyDataSetChanged()
    }

    private fun showDeleteMessageDialog(message: Message) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Message")
            .setMessage("Are you sure you want to delete this message?")
            .setPositiveButton("Delete") { _, _ ->


                val messageId = message._id

                val token = AppReference.getToken(context)
                RetrofitClient.instance.deleteMessage("Bearer $token", messageId)
                    .enqueue(object : Callback<DeleteMessageResponse>{
                        override fun onResponse(
                            call: Call<DeleteMessageResponse>,
                            response: Response<DeleteMessageResponse>
                        ) {
                            if (response.isSuccessful) {
                                val deleteResponse = response.body()
                                val message = deleteResponse?.message
                                if (deleteResponse?.status == true) {

//                                    Toast.makeText(
//                                        context,
//                                        message,
//                                        Toast.LENGTH_SHORT
//                                    ).show()

                                    removeMessageById(messageId)

                                } else {
                                    Toast.makeText(
                                        context,
                                        message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to delete message.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<DeleteMessageResponse>, t: Throwable) {
                            Toast.makeText(
                                context,
                                "Failed to delete message: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })


            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun removeMessageById(messageId: String) {
        val position = messages.indexOfFirst { it._id == messageId }
        if (position != -1) {
            (messages as MutableList).removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
