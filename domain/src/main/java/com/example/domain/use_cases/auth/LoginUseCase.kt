package com.example.domain.use_cases.auth

import com.example.domain.repositories.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {

    suspend fun execute(email: String, password: String)  = authRepository.login(email, password)
}