package com.example.sportikitochka.domain.use_cases.payment

import com.example.sportikitochka.data.models.request.payment.BuyPremiumRequest
import com.example.sportikitochka.domain.repositories.PaymentRepository

class CancelPremiumUseCase(private val paymentRepository: PaymentRepository) {

    suspend fun execute() = paymentRepository.cancelPremium()
}