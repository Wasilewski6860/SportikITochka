package com.example.data.repositories

import com.example.data.models.request.admin_action.AdminActionRequest
import com.example.data.models.request.payment.BuyPremiumRequest
import com.example.data.models.response.activities.mapToSportActivity
import com.example.data.network.PaymentApi
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectInputException
import com.example.data.network.error.IncorrectTokenException
import com.example.data.network.error.NoTokenException
import com.example.data.network.error.NotFoundException
import com.example.data.network.error.UnknownException
import com.example.domain.coroutines.Response
import com.example.domain.models.CreditCard
import com.example.domain.repositories.PaymentRepository
import com.example.domain.repositories.SessionRepository
import retrofit2.Response as RetrofitResponse

class PaymentRepositoryImpl(private val paymentApi: PaymentApi, private val sessionRepository: SessionRepository):
    PaymentRepository {
    override suspend fun getAllCards(): Response<List<CreditCard>> {
        val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val response = paymentApi.getAllCards("Bearer "+token)
                if(response.isSuccessful){
                    val data = response.body()
                    if (data != null) {
                        Response.Success(data.map { response -> CreditCard(
                            cardNumber = response.cardNumber,
                            cardName = response.cardName,
                            month = response.month,
                            year = response.year,
                            cvv = response.cvv
                        ) })
                    }
                    else Response.Failure(UnknownException())
                }
                else when(response.code()) {
                    400 -> Response.Failure(IncorrectInputException())
                    401 -> Response.Failure(IncorrectTokenException())
                    403 -> Response.Failure(ForbiddenException())
                    404 -> Response.Failure(NotFoundException())
                    else -> Response.Failure(UnknownException())
                }
            } catch (ex: Exception) {
                Response.Failure(ex)
            }
        }
        else return Response.Failure(NoTokenException())
    }

    override suspend fun buyPremium(
        cardName : String,
        cardNumber: String,
        month: Int,
        year: Int,
        cvv: Int
    ): Response<Unit> {
        val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val response = paymentApi.buyPremium("Bearer "+token, BuyPremiumRequest(cardName, cardNumber, month, year, cvv))
                if(response.isSuccessful) com.example.domain.coroutines.Response.Success(Unit)
                else when(response.code()) {
                    400 -> Response.Failure(IncorrectInputException())
                    401 -> Response.Failure(IncorrectTokenException())
                    403, 409 -> Response.Failure(ForbiddenException())
                    404 -> Response.Failure(NotFoundException())
                    else -> Response.Failure(UnknownException())
                }
            } catch (ex: Exception) {
                Response.Failure(ex)
            }
        }
        else return Response.Failure(NoTokenException())
    }

    override suspend fun cancelPremium(): Response<Unit> {
        val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val response = paymentApi.cancelPremium("Bearer "+token)
                if(response.isSuccessful) Response.Success(Unit)
                else when(response.code()) {
                    401 -> Response.Failure(IncorrectTokenException())
                    404 -> Response.Failure(NotFoundException())
                    else -> Response.Failure(UnknownException())
                }
            } catch (ex: Exception) {
                Response.Failure(ex)
            }
        }
        else return Response.Failure(NoTokenException())
    }
}