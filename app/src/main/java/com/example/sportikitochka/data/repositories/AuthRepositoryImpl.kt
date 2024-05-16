package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.request.auth.AdminRegisterRequest
import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.request.auth.RegisterRequest
import com.example.sportikitochka.data.models.response.auth.AdminRegisterResponse
import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.models.response.auth.RegisterResponse
import com.example.sportikitochka.data.models.response.auth.ValidateEmailResponse
import com.example.sportikitochka.data.network.AuthApi
import com.example.sportikitochka.domain.repositories.AuthRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import retrofit2.Response

class AuthRepositoryImpl(private val authApi: AuthApi, private val sessionRepository: SessionRepository) :
    AuthRepository {

    override suspend fun login(loginRequest: LoginRequest) = authApi.login(loginRequest)
    override suspend fun validateEmail(email: String): Response<ValidateEmailResponse> = authApi.validateEmail(email)

    override suspend fun register(email: String,registerRequest: RegisterRequest): Response<RegisterResponse> = authApi.register(email, registerRequest)
    override suspend fun register(
        email: String,
        registerRequest: AdminRegisterRequest
    ): Response<AdminRegisterResponse> = authApi.adminRegister(email, registerRequest)

    override fun saveSession(loginResponse: LoginResponse): Boolean {
        sessionRepository.saveSession(loginResponse)
        return true
    }


}
