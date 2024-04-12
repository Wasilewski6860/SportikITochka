package com.example.sportikitochka.data.models.response

import com.example.sportikitochka.domain.models.Achievement
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

fun AchievementResponse.mapToAchievement(): Achievement {
    return Achievement(
        achievementId, achievementName, achievementImage, achievementDistance
    )
}