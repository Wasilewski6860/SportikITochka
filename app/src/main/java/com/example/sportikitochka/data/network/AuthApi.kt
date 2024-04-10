package com.example.sportikitochka.data.network

import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.response.auth.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST(EndPoints.LOGIN)
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

}