package com.example.job.model

data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val imageUrl: String,
    val isAdmin: Boolean,
    val location: String,
    val phone: String,
    val resumeUrl: Any,
    val skills: List<String>,
    val updatedAt: String,
    val username: String
)