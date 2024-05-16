package com.example.sportikitochka.domain.repositories

import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.request.auth.RegisterRequest
import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.RegisterResponse
import com.example.sportikitochka.data.models.response.auth.ValidateEmailResponse
import retrofit2.Response

interface AuthRepository {

    suspend fun login(loginRequest: LoginRequest) : Response<LoginResponse>
    suspend fun validateEmail(email: String) : Response<ValidateEmailResponse>
    suspend fun register(email: String,registerRequest: RegisterRequest) : Response<RegisterResponse>
    fun saveSession(loginResponse: LoginResponse): Boolean

}