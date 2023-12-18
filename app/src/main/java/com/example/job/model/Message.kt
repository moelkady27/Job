package com.example.job.model

data class Message(
    val __v: Int,
    val _id: String,
    val chat: ChatX,
    val content: String,
    val createdAt: String,
    val receiver: Receiver,
    val sender: Sender,
    val updatedAt: String
)