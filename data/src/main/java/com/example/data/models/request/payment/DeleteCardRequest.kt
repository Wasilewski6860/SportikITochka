package com.example.data.models.request.payment

import com.google.gson.annotations.SerializedName

data class DeleteCardRequest(
    @SerializedName("card_number")
    val cardNumber: String
)