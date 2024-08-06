package com.example.data.models.response.user_data

import com.google.gson.annotations.SerializedName

data class AdminDataResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("phone")// В метрах
    val phone: String,
    @SerializedName("birthday")
    val birthday: String
)