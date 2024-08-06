package com.example.data.models.request.auth

import com.google.gson.annotations.SerializedName
import java.io.File

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("weight") val weight: Int,
    @SerializedName("avatar") val image: File,
    @SerializedName("phone") val phone: String,
    @SerializedName("birthday")val birthday: String,
    @SerializedName("password_hash") val password: String,
)