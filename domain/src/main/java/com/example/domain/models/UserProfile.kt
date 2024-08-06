package com.example.domain.models

data class UserProfile (
    val name: String,
    val image: String,
    val rating: Int,
    val statistics: Statistics,
    val achievements: List<Achievement>
)

data class Statistics(
    val totalDistanceInMeters: Long,
    val totalTime: Long,
    val totalCalories: Long
)