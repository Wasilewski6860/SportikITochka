package com.example.sportikitochka.data.models.response.profile

import com.example.sportikitochka.data.models.response.AchievementResponse
import com.google.gson.annotations.SerializedName

data class AdminProfileResponse(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String
)