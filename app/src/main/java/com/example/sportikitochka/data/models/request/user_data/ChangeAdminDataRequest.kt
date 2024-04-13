package com.example.sportikitochka.data.models.request.user_data

import com.google.gson.annotations.SerializedName

data class ChangeAdminDataRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("phone")// В метрах
    val phone: String,
    @SerializedName("birthday")
    val birthday: Long
)