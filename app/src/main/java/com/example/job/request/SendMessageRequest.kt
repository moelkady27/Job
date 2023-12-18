package com.example.job.request

data class SendMessageRequest(
    val content: String,
    val receiverId: String,
    val chatId: String
)
