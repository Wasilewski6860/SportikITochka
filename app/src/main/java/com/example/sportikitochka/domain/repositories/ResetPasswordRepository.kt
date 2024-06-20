package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.response.reset_password.CodeSendResponse
import com.example.sportikitochka.data.models.response.reset_password.ResetBaseResponse
import retrofit2.Response

interface ResetPasswordRepository {

    suspend fun sendToEmail(email: String) : Response<CodeSendResponse>
    suspend fun confirmCode(newCode: String, receivedCode: String) : Response<ResetBaseResponse>
    suspend fun changePassword(email: String,
                               oldPassword: String,
                               confirmPassword: String) : Response<ResetBaseResponse>

}