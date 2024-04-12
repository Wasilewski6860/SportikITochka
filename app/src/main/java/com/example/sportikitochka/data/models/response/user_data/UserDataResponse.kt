package com.example.sportikitochka.data.models.response.user_data

import com.google.gson.annotations.SerializedName

data class UserDataResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("weight") //В кг
    val weight: Float,
    @SerializedName("phone")// В метрах
    val phone: String,
    @SerializedName("birthday")
    val birthday: Long
)