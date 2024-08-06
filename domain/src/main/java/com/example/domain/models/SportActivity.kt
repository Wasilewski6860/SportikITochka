package com.example.domain.models


data class SportActivity(
    var id : Int =0,
    var activityType: ActivityType,
    var img: String,
    var timestamp: String,
    var avgSpeed: Float,
    var distanceInMeters: Long,
    var timeInMillis: Long,
    var caloriesBurned: Long
)