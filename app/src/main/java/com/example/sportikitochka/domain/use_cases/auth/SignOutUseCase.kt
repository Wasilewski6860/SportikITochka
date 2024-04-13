package com.example.sportikitochka.domain.use_cases.auth

import com.example.sportikitochka.domain.repositories.ActivityRepository
import com.example.sportikitochka.domain.repositories.SessionRepository

class SignOutUseCase(private val sessionRepository: SessionRepository, private val activityRepository: ActivityRepository) {

    suspend fun execute()  {
        sessionRepository.saveSession(null)
        sessionRepository.saveUserData(null)
        activityRepository.clearAll()
    }
}