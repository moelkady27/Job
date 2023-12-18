package com.example.job.request

data class ChangeRequest(
    val currentPassword: String,
    val newPassword: String
)
