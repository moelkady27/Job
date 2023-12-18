package com.example.job.model

data class UserData(
    val _id: String,
    val createdAt: String,
    val email: String,
    val imageUrl: String,
    val isAdmin: Boolean,
    val isAgent: Boolean,
    val phone: String,
    val skills: List<Any>,
    val username: String
)