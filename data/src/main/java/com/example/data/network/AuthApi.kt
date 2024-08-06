package com.example.data.network

import com.example.data.models.request.auth.LoginRequest
import com.example.data.models.response.auth.AdminRegisterResponse
import com.example.data.models.response.auth.LoginResponse
import com.example.data.models.response.auth.RegisterResponse
import com.example.data.models.response.auth.ValidateEmailResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST(EndPoints.LOGIN)
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST(EndPoints.VERIFY_EMAIL)
    suspend fun validateEmail(@Header("check_email_accessibility") email: String): Response<ValidateEmailResponse>

    @POST(EndPoints.REGISTER)
    suspend fun register(
        @Header("email") email: String,
        @Body userData: RequestBody
    ): Response<RegisterResponse>

    @POST(EndPoints.ADMIN_REGISTER)
    suspend fun adminRegister(
        @Header("email") email: String,
        @Body userData: RequestBody
    ): Response<AdminRegisterResponse>
}