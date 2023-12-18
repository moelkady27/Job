package com.example.job.model

data class UserDataX(
    val email: String,
    val imageUrl: String,
    val isAdmin: Boolean,
    val isAgent: Boolean,
    val location: String,
    val phone: String,
    val skills: List<String>,
    val username: String
)