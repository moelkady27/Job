package com.example.job.model

data class GetAllChatsResponse(
    val chats: List<Chat>,
    val status: Boolean
)