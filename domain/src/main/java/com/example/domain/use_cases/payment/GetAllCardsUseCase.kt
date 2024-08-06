package com.example.domain.use_cases.payment

import com.example.domain.repositories.PaymentRepository

class GetAllCardsUseCase(private val paymentRepository: PaymentRepository) {

    suspend fun execute() = paymentRepository.getAllCards()
}