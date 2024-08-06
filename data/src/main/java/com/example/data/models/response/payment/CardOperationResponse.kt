package com.example.data.models.response.payment

import com.google.gson.annotations.SerializedName

data class CardOperationResponse (
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Long
)