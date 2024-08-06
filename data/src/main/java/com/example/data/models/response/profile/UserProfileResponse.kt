package com.example.data.models.response.profile

import com.example.data.models.response.AchievementResponse
import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("statistics") val statisticsResponse: StatisticsResponse,
    @SerializedName("achievements") val achievements: List<AchievementResponse>
)

data class StatisticsResponse(
    @SerializedName("total_distance_in_meters") val totalDistanceInMeters: Long,
    @SerializedName("total_time") val totalTime: Long,
    @SerializedName("total_calories") val totalCalories: Long
)
