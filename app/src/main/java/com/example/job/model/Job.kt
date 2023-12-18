package com.example.job.model

data class Job(
    val __v: Int,
    val _id: String,
    val agentId: String,
    val company: String,
    val contract: String,
    val createdAt: String,
    val description: String,
    val imageUrl: String,
    val location: String,
    val period: String,
    val requirements: List<String>,
    val salary: String,
    val title: String,
    val updatedAt: String
)