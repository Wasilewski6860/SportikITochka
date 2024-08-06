package com.example.domain.use_cases.payment

import com.example.domain.repositories.PaymentRepository

class CancelPremiumUseCase(private val paymentRepository: PaymentRepository) {

    suspend fun execute() = paymentRepository.cancelPremium()
}