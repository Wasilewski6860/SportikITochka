package com.example.data.models.response.payment

import com.example.domain.models.CreditCard
import com.google.gson.annotations.SerializedName

data class CreditCardResponse(
    @SerializedName("card_name")
    var cardName: String,
    @SerializedName("card_number")
    var cardNumber: String,
    @SerializedName("month")
    var month: Int,
    @SerializedName("year")
    var year: Int,
    @SerializedName("cvv")
    var cvv: Int
)
