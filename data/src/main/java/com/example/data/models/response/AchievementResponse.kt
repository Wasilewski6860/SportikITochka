package com.example.data.models.response

import com.example.domain.models.Achievement
import com.google.gson.annotations.SerializedName

data class AchievementResponse(
    @SerializedName("id")
    val achievementId : Int,
    @SerializedName("name")
    val achievementName: String,
    @SerializedName("image")
    val achievementImage: String,
    @SerializedName("distance")
    val achievementDistance: Long
)

fun AchievementResponse.mapToAchievement(): com.example.domain.models.Achievement {
    return com.example.domain.models.Achievement(
        achievementId, achievementName, achievementImage, achievementDistance
    )
}