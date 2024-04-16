package com.example.sportikitochka.domain.use_cases.payment

import com.example.sportikitochka.data.models.request.payment.DeleteCardRequest
import com.example.sportikitochka.data.models.request.payment.EditCardRequest
import com.example.sportikitochka.domain.repositories.PaymentRepository

class EditCardUseCase(private val paymentRepository: PaymentRepository) {

    suspend fun execute(editCardRequest: EditCardRequest) = paymentRepository.editCard(editCardRequest)
}