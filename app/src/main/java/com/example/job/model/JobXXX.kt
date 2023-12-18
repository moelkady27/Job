package com.example.job.model

data class JobXXX(
    val _id: String,
    val company: String,
    val contractPeriod: String,
    val createdAt: String,
    val description: String,
    val imageUrl: String,
    val location: String,
    val requirements: List<String>,
    val salary: String,
    val title: String,
    val workHours: String
)