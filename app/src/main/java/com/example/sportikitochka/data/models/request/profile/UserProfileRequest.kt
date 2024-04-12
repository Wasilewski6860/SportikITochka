package com.example.sportikitochka.data.models.request.profile

import com.google.gson.annotations.SerializedName

data class UserProfileRequest (
    @SerializedName("period")
    val period: String //week, month, year, all time
)