package com.example.data.models.response.auth

import com.google.gson.annotations.SerializedName

data class AdminRegisterResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("admin_id")
    val userId: Int
)