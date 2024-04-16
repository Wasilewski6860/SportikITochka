package com.example.sportikitochka.domain.models

data class CreditCard(
    val cardName : String? = null,
    val cardNumber: String? = null,
    val month: Int? = null,
    val year: Int? = null,
    val cvv: Int? = null
)