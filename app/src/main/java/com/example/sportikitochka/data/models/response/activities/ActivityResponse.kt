package com.example.sportikitochka.data.models.response.activities

import com.example.sportikitochka.domain.models.SportActivity
import com.example.sportikitochka.other.mapToActivityType
import com.google.gson.annotations.SerializedName

data class ActivityResponse(
    @SerializedName("id")
    var id : Int =0,
    @SerializedName("activity_type")
    var activityType: String,
    @SerializedName("image")
    var img: String,
    @SerializedName("timestamp")
    var timestamp: Long,
    @SerializedName("avg_speed")
    var avgSpeed: Float,
    @SerializedName("distance_in_meters")
    var distanceInMeters: Long,
    @SerializedName("time_in_millis")
    var timeInMillis: Long,
    @SerializedName("calories_burned")
    var caloriesBurned: Long
)

fun ActivityResponse.mapToSportActivity(): SportActivity = SportActivity(
    id = id,
    activityType = activityType.mapToActivityType(),
    img = img,
    timestamp = timestamp,
    avgSpeed = avgSpeed,
    distanceInMeters = distanceInMeters,
    timeInMillis = timeInMillis,
    caloriesBurned = caloriesBurned
)