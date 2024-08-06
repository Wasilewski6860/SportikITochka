package com.example.data.models.response.rating

import com.example.data.models.response.users.UserResponse
import com.google.gson.annotations.SerializedName

data class RatingResponse (
    @SerializedName("users") val user: List<UserResponse>
)