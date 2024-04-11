package com.example.sportikitochka.data.models.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("error")
    val error: String
)