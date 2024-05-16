package com.example.sportikitochka.data.models.request.auth

import com.google.gson.annotations.SerializedName

data class AdminRegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val image: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("birthday")val birthday: String,
    @SerializedName("password_hash") val password: String,
)