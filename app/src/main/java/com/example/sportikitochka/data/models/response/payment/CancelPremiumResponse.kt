package com.example.sportikitochka.data.models.response.payment

import com.google.gson.annotations.SerializedName

data class CancelPremiumResponse (
    @SerializedName("success")
    val success: Boolean
)