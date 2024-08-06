package com.example.data.repositories

import com.example.data.models.request.admin_action.AdminAction
import com.example.data.models.request.admin_action.AdminActionRequest
import com.example.data.models.request.admin_action.AdminSetPremiumRequest
import com.example.data.network.AdminActionApi
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectInputException
import com.example.data.network.error.IncorrectTokenException
import com.example.data.network.error.NoTokenException
import com.example.data.network.error.NotFoundException
import com.example.data.network.error.UnknownException
import com.example.domain.coroutines.Response
import com.example.domain.repositories.AdminActionRepository
import com.example.domain.repositories.SessionRepository
import retrofit2.Response as RetrofitResponse

class AdminActionRepositoryImpl(val adminActionApi: AdminActionApi, val sessionRepository: SessionRepository):
    AdminActionRepository {

    override suspend fun adminAction(
        userId: String,
        action: String
    ): Response<Unit> {
        val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val response = adminActionApi.adminAction("Bearer "+token, AdminActionRequest(userId, action))
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

    override suspend fun grantPremium(userId: String): Response<Unit> {
       val token = sessionRepository.getSession()?.accessToken
        if(token != null) {
            return try {
                val response = adminActionApi.setPremium("Bearer "+token, AdminSetPremiumRequest(userId))
                if(response.isSuccessful) Response.Success(Unit)
                else when(response.code()) {
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
}