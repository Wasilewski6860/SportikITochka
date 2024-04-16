package com.example.sportikitochka.domain.use_cases.payment

import com.example.sportikitochka.data.models.request.admin_action.AdminAction
import com.example.sportikitochka.data.models.request.admin_action.AdminActionRequest
import com.example.sportikitochka.data.models.request.payment.AddCardRequest
import com.example.sportikitochka.domain.repositories.AdminActionRepository
import com.example.sportikitochka.domain.repositories.PaymentRepository

class AddCardUseCase(private val paymentRepository: PaymentRepository) {

    suspend fun execute(addCardRequest: AddCardRequest) = paymentRepository.addCard(addCardRequest)
}