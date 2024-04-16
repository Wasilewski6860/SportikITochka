package com.example.sportikitochka.domain.use_cases.payment

import com.example.sportikitochka.data.models.request.payment.EditCardRequest
import com.example.sportikitochka.domain.repositories.PaymentRepository

class GetAllCardsUseCase(private val paymentRepository: PaymentRepository) {

    suspend fun execute() = paymentRepository.getAllCards()
}