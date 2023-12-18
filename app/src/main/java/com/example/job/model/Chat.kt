package com.example.job.model

data class Chat(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val users: List<User>,
    val latestMessage: Message
)