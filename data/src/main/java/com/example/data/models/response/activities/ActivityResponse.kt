package com.example.data.models.response.activities

import com.example.domain.models.SportActivity
import com.example.domain.models.mapToActivityType
import com.google.gson.annotations.SerializedName

data class ActivityResponse(
    @SerializedName("id") var id : Int =0,
    @SerializedName("activity_type") var activityType: String,
    @SerializedName("image") var img: String,
    @SerializedName("date") var timestamp: String,
    @SerializedName("avg_speed") var avgSpeed: Float,
    @SerializedName("distance_in_meters") var distanceInMeters: Long,
    @SerializedName("duration") var timeInMillis: Long,
    @SerializedName("calories_burned") var caloriesBurned: Long
)

fun ActivityResponse.mapToSportActivity(): com.example.domain.models.SportActivity =
    com.example.domain.models.SportActivity(
        id = id,
        activityType = activityType.mapToActivityType(),
        img = img,
        timestamp = timestamp,
        avgSpeed = avgSpeed,
        distanceInMeters = distanceInMeters,
        timeInMillis = timeInMillis,
        caloriesBurned = caloriesBurned
    )