package com.example.sportikitochka.domain.use_cases.auth

import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.data.models.request.auth.RegisterRequest
import com.example.sportikitochka.domain.repositories.AuthRepository

class RegisterUseCase(private val authRepository: AuthRepository) {

    suspend fun execute(email: String,registerRequest: RegisterRequest)  = authRepository.register(email,registerRequest)
}