package com.example.sportikitochka.data.models.response.payment

import com.google.gson.annotations.SerializedName

data class BuyPremiumResponse (
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Long
)