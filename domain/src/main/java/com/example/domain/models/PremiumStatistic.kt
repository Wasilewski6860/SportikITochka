package com.example.domain.models

data class PremiumStatistic(
    val totalDistanceInMeters: Long,
    val totalTime: Long,
    val totalCalories: Long,
    val avgSpeed: Float,
    val activities: List<SportActivityStatistic>
)

data class SportActivityStatistic(
    val id: Int,
    val activityType: String,
    val date: String,
    val avgSpeed: Float,
    val timestamp: Long,
    val distanceInMeters: Int,
    val timeInMillis: Long,
    val caloriesBurned: Int
)