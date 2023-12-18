package com.example.job.request

data class UpdateRequest(
    val location: String,
    val phone: String,
    val imageUrl: String,
    val skills: List<String>
)
