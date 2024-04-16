package com.example.sportikitochka.data.network

import com.example.sportikitochka.data.models.request.payment.AddCardRequest
import com.example.sportikitochka.data.models.request.payment.BuyPremiumRequest
import com.example.sportikitochka.data.models.request.payment.DeleteCardRequest
import com.example.sportikitochka.data.models.request.payment.EditCardRequest
import com.example.sportikitochka.data.models.response.payment.BuyPremiumResponse
import com.example.sportikitochka.data.models.response.payment.CancelPremiumResponse
import com.example.sportikitochka.data.models.response.payment.CardOperationResponse
import com.example.sportikitochka.data.models.response.payment.CreditCardResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PaymentApi {

    @POST(EndPoints.GET_ALL_CARDS)
    suspend fun getAllCards(
        @Header("аuthorization") token: String
    ): Response<List<CreditCardResponse>>

    @POST(EndPoints.ADD_CARD)
    suspend fun addCard(
        @Header("аuthorization") token: String,
        @Body addCardRequest: AddCardRequest,
    ): Response<CardOperationResponse>

    @POST(EndPoints.EDIT_CARD)
    suspend fun editCard(
        @Header("аuthorization") token: String,
        @Body editCardRequest: EditCardRequest,
    ): Response<CardOperationResponse>

    @POST(EndPoints.DELETE_CARD)
    suspend fun deleteCard(
        @Header("аuthorization") token: String,
        @Body deleteCardRequest: DeleteCardRequest
    ): Response<CardOperationResponse>


    @POST(EndPoints.BUY_PREMIUM)
    suspend fun buyPremium(
        @Header("аuthorization") token: String,
        @Body buyPremiumRequest: BuyPremiumRequest
    ): Response<BuyPremiumResponse>

    @POST(EndPoints.CANCEL_PREMIUM)
    suspend fun cancelPremium(
        @Header("аuthorization") token: String
    ): Response<CancelPremiumResponse>
}