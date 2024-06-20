package com.example.sportikitochka.data.network

import com.example.sportikitochka.data.models.request.reset_password.ConfirmCodeRequest
import com.example.sportikitochka.data.models.request.reset_password.ResetPasswordRequest
import com.example.sportikitochka.data.models.response.reset_password.CodeSendResponse
import com.example.sportikitochka.data.models.response.reset_password.ResetBaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ResetPasswordApi {

    @POST(EndPoints.SEND_TO_EMAIL)
    suspend fun sendToEmail(
        @Header("email") email: String
    ): Response<CodeSendResponse>

    @POST(EndPoints.CONFIRM_CODE)
    suspend fun confirmCode(
        @Body newCode: ConfirmCodeRequest
    ): Response<ResetBaseResponse>

    @PUT(EndPoints.CHANGE_PASSWORD)
    suspend fun changePassword(
        @Header("email") email: String,
        @Body data: ResetPasswordRequest
    ): Response<ResetBaseResponse>

}