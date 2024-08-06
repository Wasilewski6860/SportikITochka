package com.example.data.models.response.statistic

import com.google.gson.annotations.SerializedName

data class PremiumStatisticsResponse(
    @SerializedName("total_distance_in_meters") val totalDistanceInMeters: Long,
    @SerializedName("total_time") val totalTime: Long,
    @SerializedName("total_calories") val totalCalories: Long,
    @SerializedName("avg_speed") val avgSpeed: Float,
    @SerializedName("activities") val activities: List<SportActivityStatistic>
)

data class SportActivityStatistic(
    @SerializedName("id") val id: Int,
    @SerializedName("activity_type") val activityType: String,
    @SerializedName("date") val date: String,
    @SerializedName("avg_speed") val avgSpeed: Float,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("distance_in_meters") val distanceInMeters: Int,
    @SerializedName("time_in_millis") val timeInMillis: Long,
    @SerializedName("calories_burned") val caloriesBurned: Int
)
