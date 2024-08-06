package com.example.domain.use_cases.user_data

import com.example.domain.repositories.SessionRepository

class GetUserDataLocallyUseCase(private val sessionRepository: SessionRepository) {

    fun execute() = sessionRepository.getUserData()
}