package com.example.sportikitochka.data.network

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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface PaymentApi {

    @GET(EndPoints.GET_ALL_CARDS)
    suspend fun getAllCards(
        @Header("Authorization") token: String
    ): Response<List<CreditCardResponse>>


    @POST(EndPoints.BUY_PREMIUM)
    suspend fun buyPremium(
        @Header("Authorization") token: String,
        @Body buyPremiumRequest: BuyPremiumRequest
    ): Response<BuyPremiumResponse>

    @PUT(EndPoints.CANCEL_PREMIUM)
    suspend fun cancelPremium(
        @Header("Authorization") token: String
    ): Response<CancelPremiumResponse>
}