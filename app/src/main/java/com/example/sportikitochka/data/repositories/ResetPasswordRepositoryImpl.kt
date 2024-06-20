package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.request.reset_password.ConfirmCodeRequest
import com.example.sportikitochka.data.models.request.reset_password.ResetPasswordRequest
import com.example.sportikitochka.data.models.response.reset_password.CodeSendResponse
import com.example.sportikitochka.data.models.response.reset_password.ResetBaseResponse
import com.example.sportikitochka.data.network.ResetPasswordApi
import com.example.sportikitochka.data.network.UserProfileApi
import com.example.sportikitochka.domain.repositories.ResetPasswordRepository
import retrofit2.Response

class ResetPasswordRepositoryImpl(val resetPasswordApi: ResetPasswordApi): ResetPasswordRepository {
    override suspend fun sendToEmail(email: String): Response<CodeSendResponse> {
        return resetPasswordApi.sendToEmail(email)
    }

    override suspend fun confirmCode(newCode: String, receivedCode: String): Response<ResetBaseResponse> {
        return resetPasswordApi.confirmCode(ConfirmCodeRequest(newCode, receivedCode))
    }

    override suspend fun changePassword(
        email: String,
        oldPassword: String,
        confirmPassword: String
    ): Response<ResetBaseResponse> {
        return resetPasswordApi.changePassword(email,ResetPasswordRequest(oldPassword, confirmPassword))
    }
}