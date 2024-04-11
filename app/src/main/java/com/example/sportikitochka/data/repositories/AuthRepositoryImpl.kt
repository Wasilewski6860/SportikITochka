package com.example.sportikitochka.data.repositories

import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.data.network.AuthApi
import com.example.sportikitochka.domain.repositories.AuthRepository
import com.example.sportikitochka.domain.repositories.SessionRepository
import retrofit2.Response

class AuthRepositoryImpl(private val authApi: AuthApi, private val sessionRepository: SessionRepository) :
    AuthRepository {

    override suspend fun login(loginRequest: LoginRequest) = authApi.login(loginRequest)
    override fun saveSession(loginResponse: LoginResponse): Boolean {
        sessionRepository.saveSession(loginResponse)
        return true
    }


}
