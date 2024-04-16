package com.example.sportikitochka.data.models.response.payment

import com.example.sportikitochka.domain.models.CreditCard
import com.google.gson.annotations.SerializedName

data class CreditCardResponse(
    @SerializedName("card_name")
    var cardName : String,
    @SerializedName("card_number")
    var cardNumber: String,
    @SerializedName("month")
    var month: Int,
    @SerializedName("year")
    var year: Int,
    @SerializedName("cvv")
    var cvv: Int
)

fun CreditCardResponse.mapToCreditCard(): CreditCard = CreditCard(cardName, cardNumber, month, year, cvv)