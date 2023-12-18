package com.example.job.request

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String,
    val phone: String,
    val skills: List<String>,
    val resumeUrl: String,
    val imageUrl: String
)