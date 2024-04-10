package com.example.sportikitochka.domain.use_cases.auth

import com.example.sportikitochka.domain.repositories.SessionRepository

class IsLoggedUseCase(private val sessionRepository: SessionRepository) {
    fun execute()  = sessionRepository.getSession() != null
}