package com.example.sportikitochka.data.models.request.payment

import com.google.gson.annotations.SerializedName

data class EditCardRequest(
    @SerializedName("card_name")
    val cardName : String,
    @SerializedName("card_number")
    val cardNumber: String,
    @SerializedName("month")
    val month: Int,
    @SerializedName("year")
    val year: Int,
    @SerializedName("cvv")
    val cvv: Int
)