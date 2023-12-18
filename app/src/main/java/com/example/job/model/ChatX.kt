package com.example.job.model

data class ChatX(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val latestMessage: LatestMessage,
    val updatedAt: String,
    val users: List<UserX>
)