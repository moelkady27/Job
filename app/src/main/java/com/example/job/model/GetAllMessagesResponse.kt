package com.example.job.model

data class GetAllMessagesResponse(
    val messages: List<Message>,
    val status: Boolean
)