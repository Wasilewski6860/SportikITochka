package com.example.sportikitochka.data.models.response.auth

import com.google.gson.annotations.SerializedName

data class ValidateEmailResponse(
    @SerializedName("free")
    val free: Boolean
)