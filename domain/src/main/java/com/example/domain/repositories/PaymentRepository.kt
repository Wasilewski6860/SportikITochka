package com.example.domain.repositories

import com.example.domain.coroutines.Response
import com.example.domain.models.CreditCard


interface PaymentRepository {

    suspend fun getAllCards() : Response<List<CreditCard>>
    suspend fun buyPremium(
        cardName : String,
        cardNumber: String,
        month: Int,
        year: Int,
        cvv: Int
    ) : Response<Unit>

    suspend fun cancelPremium() : Response<Unit>
}