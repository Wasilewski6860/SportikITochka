package com.example.sportikitochka.domain.use_cases.auth

import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.domain.repositories.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {

    suspend fun execute(loginRequest: LoginRequest)  = authRepository.login(loginRequest)
}