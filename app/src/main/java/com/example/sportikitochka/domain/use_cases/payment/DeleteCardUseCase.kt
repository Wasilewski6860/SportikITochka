package com.example.sportikitochka.domain.use_cases.payment

import com.example.sportikitochka.data.models.request.payment.BuyPremiumRequest
import com.example.sportikitochka.data.models.request.payment.DeleteCardRequest
import com.example.sportikitochka.domain.repositories.PaymentRepository

class DeleteCardUseCase(private val paymentRepository: PaymentRepository) {

    suspend fun execute(deleteCardRequest: DeleteCardRequest) = paymentRepository.deleteCard(deleteCardRequest)
}