package com.example.sportikitochka.data.network

import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.request.auth.RegisterRequest
import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.RegisterResponse
import com.example.sportikitochka.data.models.response.auth.ValidateEmailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST(EndPoints.LOGIN)
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
    @POST(EndPoints.VERIFY_EMAIL)
    suspend fun validateEmail(@Header("check_email_accessibility") email: String): Response<ValidateEmailResponse>

    @POST(EndPoints.REGISTER)
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
}