package com.example.domain.use_cases.auth

import com.example.domain.repositories.ActivityRepository
import com.example.domain.repositories.OnboardingRepository
import com.example.domain.repositories.SessionRepository

class SignOutUseCase(
    private val onboardingRepository: OnboardingRepository,
    private val sessionRepository: SessionRepository,
    private val activityRepository: ActivityRepository
) {

    suspend fun execute()  {
        onboardingRepository.setViewed(false)
        sessionRepository.saveSession(null)
        sessionRepository.saveUserData(null)
        activityRepository.clearAll()
    }
}