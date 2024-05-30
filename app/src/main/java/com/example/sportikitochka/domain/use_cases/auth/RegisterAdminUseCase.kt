package com.example.sportikitochka.domain.use_cases.auth

import com.example.sportikitochka.data.models.request.auth.AdminRegisterRequest
import com.example.sportikitochka.data.models.request.auth.RegisterRequest
import com.example.sportikitochka.domain.repositories.AuthRepository

class RegisterAdminUseCase(private val authRepository: AuthRepository) {

    suspend fun execute(email: String,registerRequest: AdminRegisterRequest)  = authRepository.register(email,registerRequest)
}