package com.example.data.models.response.payment

import com.google.gson.annotations.SerializedName

data class AllCardsResponse (
    @SerializedName("card_numbers")
    val cardNumbers: List<String>,

    @SerializedName("card_ids")
    val cardIds: List<Int>
)