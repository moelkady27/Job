package com.example.job.model

data class JobsResponse(
    val jobs: List<Job>,
    val status: Boolean
)