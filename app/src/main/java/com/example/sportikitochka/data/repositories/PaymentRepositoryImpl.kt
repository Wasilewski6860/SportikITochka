package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.request.payment.AddCardRequest
import com.example.sportikitochka.data.models.request.payment.BuyPremiumRequest
import com.example.sportikitochka.data.models.request.payment.DeleteCardRequest
import com.example.sportikitochka.data.models.request.payment.EditCardRequest
import com.example.sportikitochka.data.models.response.payment.AllCardsResponse
import com.example.sportikitochka.data.models.response.payment.BuyPremiumResponse
import com.example.sportikitochka.data.models.response.payment.CancelPremiumResponse
import com.example.sportikitochka.data.models.response.payment.CardOperationResponse
import com.example.sportikitochka.data.models.response.payment.CreditCardResponse
import com.example.sportikitochka.data.network.PaymentApi
import com.example.sportikitochka.domain.repositories.PaymentRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import retrofit2.Response

class PaymentRepositoryImpl(private val paymentApi: PaymentApi, private val sessionRepository: SessionRepository): PaymentRepository {
    override suspend fun getAllCards(): Response<AllCardsResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        return paymentApi.getAllCards("Bearer "+token)
    }

    override suspend fun buyPremium(buyPremiumRequest: BuyPremiumRequest): Response<BuyPremiumResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        return paymentApi.buyPremium("Bearer "+token, buyPremiumRequest)
    }

    override suspend fun cancelPremium(): Response<CancelPremiumResponse> {
        val token = sessionRepository.getSession()!!.accessToken
        return paymentApi.cancelPremium("Bearer "+token)
    }
}