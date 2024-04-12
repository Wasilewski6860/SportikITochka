package com.example.sportikitochka.domain.models

import com.example.sportikitochka.other.ActivityType

data class SportActivity(
    var id : Int =0,
    var activityType: ActivityType,
    var img: String,
    var timestamp: Long,
    var avgSpeed: Float,
    var distanceInMeters: Long,
    var timeInMillis: Long,
    var caloriesBurned: Long
)