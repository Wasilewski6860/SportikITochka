package com.example.sportikitochka.domain.use_cases.user_data

import com.example.sportikitochka.domain.repositories.SessionRepository

class GetUserDataLocallyUseCase(private val sessionRepository: SessionRepository) {

    fun execute() = sessionRepository.getUserData()
}