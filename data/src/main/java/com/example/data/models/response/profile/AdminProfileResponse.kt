package com.example.data.models.response.profile

import com.google.gson.annotations.SerializedName

data class AdminProfileResponse(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String
)