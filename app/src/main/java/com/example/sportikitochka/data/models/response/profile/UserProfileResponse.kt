package com.example.sportikitochka.data.models.response.profile

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("statistics") val statistics: Statistics,
    @SerializedName("achievements") val achievements: List<Achievement>
)

data class Statistics(
    @SerializedName("total_distance_in_meters") val totalDistanceInMeters: Long,
    @SerializedName("total_time") val totalTime: Long,
    @SerializedName("total_calories") val totalCalories: Long
)

data class Achievement(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("distance") val distance: Long
)