package com.example.job.model

data class LoginResponse(
    val status: Boolean,
    val token: String,
    val userData: UserData
)