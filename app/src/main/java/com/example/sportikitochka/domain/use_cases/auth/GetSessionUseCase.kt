package com.example.sportikitochka.domain.use_cases.auth

import com.example.sportikitochka.data.models.response.auth.LoginResponse
import com.example.sportikitochka.domain.repositories.AuthRepository
import com.example.sportikitochka.domain.repositories.SessionRepository

class GetSessionUseCase(private val sessionRepository: SessionRepository) {

    fun execute()  = sessionRepository.getSession()
}