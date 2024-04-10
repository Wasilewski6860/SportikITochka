package com.example.sportikitochka.domain.use_cases.auth

import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.domain.repositories.AuthRepository

class SaveSessionUseCase(private val authRepository: AuthRepository) {

    fun execute(loginResponse: LoginResponse)  = authRepository.saveSession(loginResponse)
}