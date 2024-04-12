package com.example.sportikitochka.domain.use_cases.auth

import com.example.sportikitochka.data.models.request.auth.LoginRequest
import com.example.sportikitochka.domain.repositories.AuthRepository

class ValidateEmailUseCase(private val authRepository: AuthRepository) {

    suspend fun execute(email: String)  = authRepository.validateEmail(email)
}