package com.example.domain.use_cases.onboarding

import com.example.domain.repositories.OnboardingRepository

class IsOnboardingViewedUseCase(private val onboardingRepository: OnboardingRepository) {

    fun execute() = onboardingRepository.getViewed()
}