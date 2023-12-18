package com.example.job.request

data class CreateJobRequest(
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
