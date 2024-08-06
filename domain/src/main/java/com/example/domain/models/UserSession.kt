package com.example.domain.models

data class UserSession(
    var accessToken: String,
    var role: String,
    var userId: Int,
)