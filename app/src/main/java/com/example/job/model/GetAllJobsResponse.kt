package com.example.job.model

data class GetAllJobsResponse(
    val jobs: List<JobXX>,
    val status: Boolean
)