package com.example.sportikitochka.data.models.request.activities

import com.google.gson.annotations.SerializedName

data class AddActivityRequest(

    @SerializedName("activity_type") var activityType: String,
    @SerializedName("image") var img: String,
    @SerializedName("date") var timestamp: String,
    @SerializedName("avg_speed") var avgSpeed: Float,
    @SerializedName("distance_in_meters") var distanceInMeters: Long,
    @SerializedName("duration") var timeInMillis: Long,
    @SerializedName("calories_burned") var caloriesBurned: Long
)