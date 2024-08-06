package com.example.domain.use_cases.auth

import com.example.domain.repositories.AuthRepository

class ValidateEmailUseCase(private val authRepository: AuthRepository) {

    suspend fun execute(email: String)  = authRepository.validateEmail(email)
}