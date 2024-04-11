package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.response.auth.LoginResponse
import retrofit2.Response

interface AuthRepository {

    suspend fun login(loginRequest: LoginRequest) : Response<LoginResponse>
    fun saveSession(loginResponse: LoginResponse): Boolean

}