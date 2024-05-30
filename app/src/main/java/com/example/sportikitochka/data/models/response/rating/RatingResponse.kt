package com.example.sportikitochka.data.models.response.rating

import com.example.sportikitochka.data.models.response.users.UserResponse
import com.google.gson.annotations.SerializedName

data class RatingResponse (
    @SerializedName("users") val user: List<UserResponse>
)