package com.example.sportikitochka.data.models.response.users

import com.example.sportikitochka.data.models.response.AchievementResponse
import com.example.sportikitochka.data.models.response.auth.UserType.Admin.toUserType
import com.example.sportikitochka.data.models.response.mapToAchievement
import com.example.sportikitochka.domain.models.User
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: String,
    @SerializedName("image") val image: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("total_activities_count") val totalCount: Int,
    @SerializedName("total_distance_in_meters") val totalDistanse: Float,
    @SerializedName("total_time") val totalTime: Long,
    @SerializedName("total_calories") val totalCalories: Long,
    @SerializedName("avg_speed") val averageSpeed: Float,
    @SerializedName("average_distance_in_meters") val averageDistanse: Float,
    @SerializedName("average_time") val averageTime: Long,
    @SerializedName("average_calories") val averageCalories: Long,
    @SerializedName("is_blocked") val isBlocked: Boolean,
    @SerializedName("achievements") val achievements: List<AchievementResponse>,
)


fun UserResponse.mapToUser(): User {
    return User(
        id = this.id,
        name = this.name,
        role = this.role.toUserType(),
        image = this.image,
        place = this.rating,
        totalCount = this.totalCount,
        totalDistanse = this.totalDistanse,
        totalTime = this.totalTime,
        totalCalories = this.totalCalories,
        averageDistanse = this.averageDistanse,
        averageTime = this.averageTime,
        averageCalories = this.averageCalories,
        achievements = this.achievements.map { it -> it.mapToAchievement() },
        isBlocked = this.isBlocked
    )
}