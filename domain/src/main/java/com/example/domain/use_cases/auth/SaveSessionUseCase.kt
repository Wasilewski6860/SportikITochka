package com.example.domain.use_cases.auth

import com.example.domain.models.UserSession
import com.example.domain.repositories.AuthRepository

class SaveSessionUseCase(private val authRepository: AuthRepository) {

    fun execute(loginResponse: UserSession)  = authRepository.saveSession(loginResponse)
}