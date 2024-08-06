package com.example.domain.use_cases.payment

import com.example.domain.repositories.PaymentRepository

class BuyPremiumUseCase(private val paymentRepository: PaymentRepository) {

    suspend fun execute(
        cardName: String,
        cardNumber: String,
        month: Int,
        year: Int,
        cvv: Int
    ) = paymentRepository.buyPremium(
        cardName = cardName,
        cardNumber = cardNumber,
        month = month,
        year = year,
        cvv = cvv
    )
}