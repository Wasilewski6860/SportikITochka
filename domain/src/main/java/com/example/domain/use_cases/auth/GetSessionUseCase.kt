package com.example.domain.use_cases.auth

import com.example.domain.repositories.AuthRepository
import com.example.domain.repositories.SessionRepository

class GetSessionUseCase(private val sessionRepository: SessionRepository) {

    fun execute()  = sessionRepository.getSession()
}