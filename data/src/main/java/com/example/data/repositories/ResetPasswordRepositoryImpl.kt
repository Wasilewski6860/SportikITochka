package com.example.data.repositories

import com.example.data.models.request.reset_password.ConfirmCodeRequest
import com.example.data.models.request.reset_password.ResetPasswordRequest
import com.example.data.network.ResetPasswordApi
import com.example.data.network.error.DoesntMatchException
import com.example.data.network.error.ForbiddenException
import com.example.data.network.error.IncorrectTokenException
import com.example.data.network.error.NoTokenException
import com.example.data.network.error.NotFoundException
import com.example.data.network.error.UnknownException
import com.example.domain.coroutines.Response
import com.example.domain.models.Achievement
import com.example.domain.models.ConfirmationCode
import com.example.domain.models.Statistics
import com.example.domain.models.UserProfile
import com.example.domain.repositories.ResetPasswordRepository
import retrofit2.Response as RetrofitResponse

class ResetPasswordRepositoryImpl(val resetPasswordApi: ResetPasswordApi):
    ResetPasswordRepository {
    override suspend fun sendToEmail(email: String): Response<ConfirmationCode> {
        return try {
            val response = resetPasswordApi.sendToEmail(email)
            if(response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    Response.Success(
                        ConfirmationCode(data.message, data.code)
                    )
                }
                else Response.Failure(UnknownException())
            }
            else when(response.code()) {
                404 -> Response.Failure(NotFoundException())
                else -> Response.Failure(UnknownException())
            }
        } catch (ex: Exception) {
            Response.Failure(ex)
        }
    }

    override suspend fun confirmCode(newCode: String, receivedCode: String): Response<Unit> {
        return try {
            val response = resetPasswordApi.confirmCode(ConfirmCodeRequest(newCode, receivedCode))
            if(response.isSuccessful) {
                Response.Success(
                    Unit
                )
            }
            else when(response.code()) {
                403 -> Response.Failure(ForbiddenException())
                else -> Response.Failure(UnknownException())
            }
        } catch (ex: Exception) {
            Response.Failure(ex)
        }
    }

    override suspend fun changePassword(
        email: String,
        oldPassword: String,
        confirmPassword: String
    ): Response<Unit> {
        return try {
            val response = resetPasswordApi.changePassword(email,
                ResetPasswordRequest(oldPassword, confirmPassword)
            )
            if(response.isSuccessful) {
                Response.Success(
                    Unit
                )
            }
            else when(response.code()) {
                422 -> Response.Failure(DoesntMatchException())
                else -> Response.Failure(UnknownException())
            }
        } catch (ex: Exception) {
            Response.Failure(ex)
        }
    }
}