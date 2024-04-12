package com.example.sportikitochka.data.models.request.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("weight") //В кг
    val weight: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("birthday")// timestamp
    val birthday: Long,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)