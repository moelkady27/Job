package com.example.job.request

data class UpdateJobRequest(
    val id: String,
    val title: String,
    val location: String,
    val company: String,
    val description: String,
    val salary: String,
    val workHours: String,
    val contractPeriod: String,
    val imageUrl: String,
    val requirements: List<String>
)
