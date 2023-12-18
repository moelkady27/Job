package com.example.job.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.job.AppReference
import com.example.job.R
import com.example.job.adapter.MessagesAdapter
import com.example.job.model.GetAllMessagesResponse
import com.example.job.model.SendMessageResponse
import com.example.job.request.SendMessageRequest
import com.example.job.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_chatting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChattingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessagesAdapter

    // pagination
    private var currentPage = 1
    private var isLoading = false
    private var scrollToLatestMessage = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        setUpActionBar()

        val chatId = intent.getStringExtra("chatId")
        val receiverId = intent.getStringExtra("otherUserId")
        val receiverUsername = intent.getStringExtra("receiverUsername")
        val receiverImage = intent.getStringExtra("receiverImage")
        val currentUserId = AppReference.getUserId(this@ChattingActivity)

        imageViewSend.setOnClickListener {
            sendMessage(chatId, receiverId)
        }

        tv_username.text = receiverUsername

        Glide.with(this)
            .load(receiverImage)
            .into(toolbars_image)

        recyclerView = findViewById(R.id.r_view)
        adapter = MessagesAdapter(emptyList(), currentUserId, this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        // pagination
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (lastVisibleItemPosition == totalItemCount - 1 && !isLoading) {
                    currentPage++
                    fetchMessages(chatId, currentPage)
                }

                if (dy < 0) {
                    scrollToLatestMessage = false
                }
            }
        })

        fetchMessages(chatId, currentPage)
    }

    private fun fetchMessages(chatId: String?, page: Int) {
        isLoading = true

        val token = AppReference.getToken(this)

        RetrofitClient.instance.getAllMessages("Bearer $token", chatId.toString(), page.toString())
            .enqueue(object : Callback<GetAllMessagesResponse> {
                override fun onResponse(
                    call: Call<GetAllMessagesResponse>,
                    response: Response<GetAllMessagesResponse>
                ) {
                    isLoading = false

                    if (response.isSuccessful) {
                        val messagesResponse = response.body()
                        val messages = messagesResponse?.messages ?: emptyList()

                        adapter.addMessages(messages)

                        if (scrollToLatestMessage) {
                            recyclerView.scrollToPosition(adapter.itemCount - 1)
                        }
                    } else {
                        val error = response.errorBody()?.string()
                        Toast.makeText(
                            this@ChattingActivity,
                            error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<GetAllMessagesResponse>, t: Throwable) {
                    isLoading = false

                    Toast.makeText(
                        this@ChattingActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun sendMessage(chatId: String?, receiverId: String?) {
        val token = AppReference.getToken(this)

        val messageContent = editTextTextSendChat.text.toString()

        val request = SendMessageRequest(messageContent, receiverId.toString(), chatId.toString())

        RetrofitClient.instance.sendMessage("Bearer $token", request)
            .enqueue(object : Callback<SendMessageResponse> {
                override fun onResponse(
                    call: Call<SendMessageResponse>,
                    response: Response<SendMessageResponse>
                ) {
                    if (response.isSuccessful) {
                        val sendMessageResponse = response.body()
                        val message = sendMessageResponse?.message
                        if (sendMessageResponse != null && sendMessageResponse.status) {

                            editTextTextSendChat.text.clear()

                            Log.e("message is : ", messageContent)

                            scrollToLatestMessage = true

                            fetchMessages(chatId, currentPage)

                        } else {
                            Toast.makeText(
                                this@ChattingActivity,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@ChattingActivity,
                            "Failed to send message",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SendMessageResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ChattingActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbars)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_white_24)
        }

        toolbars.setNavigationOnClickListener {
            startActivity(Intent(this@ChattingActivity, ChatActivity::class.java))
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
        finish()
    }
}