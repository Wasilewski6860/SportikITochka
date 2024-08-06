package com.example.data.models.response.auth

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("access_token")
    var accessToken: String,
    @SerializedName("role")
    var role: String,
    @SerializedName("user_id")
    var userId: Int,
)
