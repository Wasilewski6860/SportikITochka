package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.request.payment.AddCardRequest
import com.example.sportikitochka.data.models.request.payment.BuyPremiumRequest
import com.example.sportikitochka.data.models.request.payment.DeleteCardRequest
import com.example.sportikitochka.data.models.request.payment.EditCardRequest
import com.example.sportikitochka.data.models.response.payment.AllCardsResponse
import com.example.sportikitochka.data.models.response.payment.BuyPremiumResponse
import com.example.sportikitochka.data.models.response.payment.CancelPremiumResponse
import com.example.sportikitochka.data.models.response.payment.CardOperationResponse
import com.example.sportikitochka.data.models.response.payment.CreditCardResponse
import retrofit2.Response

interface PaymentRepository {

    suspend fun getAllCards() : Response<List<CreditCardResponse>>
    suspend fun buyPremium(buyPremiumRequest: BuyPremiumRequest) : Response<BuyPremiumResponse>
    suspend fun cancelPremium() : Response<CancelPremiumResponse>
}