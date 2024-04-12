package com.example.sportikitochka.data.models.request.user_data

import com.google.gson.annotations.SerializedName

data class ChangeDataUserRequest(
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